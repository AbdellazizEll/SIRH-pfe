import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl, ValidationErrors } from "@angular/forms";
import { AuthService } from "../../../_services/auth.service";
import { Router, ActivatedRoute } from "@angular/router";
import { CollaboratorService } from "../../../_services/collaborator.service";
import {AppComponent} from "../../../app.component";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  changePasswordForm!: FormGroup<{
    currentPassword: FormControl<string>;
    newPassword: FormControl<string>;
    confirmationPassword: FormControl<string>;
  }>;

  submitted = false;
  successMessage: string = '';
  errorMessage: string = '';
  infoMessage: string = ''; // New message to display for the redirect reason

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute, // To access query parameters
    private collabService: CollaboratorService,
    private appComponent: AppComponent // Inject AppComponent to access accueilLink

  ) {}

  ngOnInit(): void {
    this.initializeForm();

    // Get the message from the query params and set the infoMessage
    this.route.queryParams.subscribe(params => {
      this.infoMessage = params['message'] || 'Veuillez changer votre mot de passe pour continuer.';
      console.log("Info message set: ", this.infoMessage); // Debugging line
    });
  }
  private initializeForm(): void {
    this.changePasswordForm = this.fb.group({
      currentPassword: new FormControl<string>('', [Validators.required]),
      newPassword: new FormControl<string>('', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=]).+$/)
      ]),
      confirmationPassword: new FormControl<string>('', [Validators.required])
    }, {
      validators: this.passwordMatchValidator
    });
  }

  get f() { return this.changePasswordForm.controls; }

  passwordMatchValidator(form: AbstractControl): ValidationErrors | null {
    const newPassword = form.get('newPassword')?.value;
    const confirmationPassword = form.get('confirmationPassword')?.value;

    if (newPassword !== confirmationPassword) {
      return { passwordMismatch: true };
    } else {
      return null;
    }
  }

  onSubmit(): void {
    this.submitted = true;
    this.successMessage = '';
    this.errorMessage = '';

    if (this.changePasswordForm.invalid) {
      return;
    }

    const formData = this.changePasswordForm.value;

    this.collabService.changePassword(formData).subscribe({
      next: (response) => {
        // Assuming the response has a structure like { message: 'Password changed successfully.' }
        if (response && response.message) {
          this.successMessage = response.message;
        } else {
          this.successMessage = 'Votre mot de passe a été changé avec succès.'; // Fallback message
        }

        this.changePasswordForm.reset();
        this.submitted = false;

        // Redirect after successful password change
        const redirectLink = this.appComponent.getAccueilLink();
        setTimeout(() => {
          this.router.navigate([redirectLink]);
        }, 2000);
      },
      error: (error) => {
        // Handle error and display appropriate message
        if (error.error && typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else if (error.error && error.error.message) {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = 'Une erreur est survenue. Veuillez réessayer.';
        }
      }
    });
  }
}
