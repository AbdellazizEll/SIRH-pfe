import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { map } from "rxjs/operators";
import { AbsenceService } from "../../../_services/absence.service";
import { TokenStorageService } from "../../../_services/token-storage.service";
import { DemandeAbsence } from "../../../domain/DemandeAbsence";
import { Router } from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";

@Component({
  selector: 'app-ajout-absence',
  templateUrl: './ajout-absence.component.html',
  styleUrls: ['./ajout-absence.component.scss']
})
export class AjoutAbsenceComponent implements OnInit {
  private absenceTypeMapping: { [key: string]: string } = {
    'PAID_LEAVE': 'Payé',
    'UNPAID_LEAVE': 'Impayé',
    'SICK_LEAVE': 'Maladie',
    'MATERNITY_LEAVE': 'Maternité',
    'PARENTAL_LEAVE': 'Parentale',
    'STUDY_LEAVE': 'Etudes',
    'MARRIAGE_LEAVE': 'Marriage',
    'EMERGENCY_LEAVE': 'Urgence'
  };
  absenceForm: FormGroup;
  absenceMotif: any[] = [];
  minDate: string;
  successMessage: string = '';
  errorMessage: string = '';
  showFileUpload: boolean = false;
  justificatif: File | null = null;
  formSubmitted: boolean = false;

  constructor(private fb: FormBuilder, private absenceService: AbsenceService,
              private router: Router, private tokenStorage: TokenStorageService,
              private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    const today = new Date();
    this.minDate = today.toISOString().split('T')[0];

    this.absenceForm = this.fb.group({
      absenceReason: ['', Validators.required],
      startDate: ['', [Validators.required, this.dateValidator.bind(this)]],
      endDate: ['', [Validators.required, this.dateValidator.bind(this)]],
      comment: ['', Validators.required],
      justificatif: [null]
    });

    // Now fetch motifs
    this.fetchMotifs();
  }

  fetchMotifs() {
    return this.absenceService.getAbsenceMotifs()
      .pipe(map(data => {
        for (let item of data) {
          this.absenceMotif.push({
            id: item.absence,
            value: item.typeAbs
          });
        }
        if (data.status === 'OK') {
          this.absenceMotif = data.value;
          if (this.absenceForm.get('absenceReason')) {
            this.absenceForm.get('absenceReason').patchValue(this.absenceMotif);
          }
        }
      }))
      .subscribe(() => console.log(this.absenceMotif));
  }

  dateValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const startDateControl = this.absenceForm?.get('startDate');
    const currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);

    if (control === startDateControl && new Date(control.value) < currentDate) {
      return { 'invalidStartDate': true }; // Start date cannot be in the past
    }

    const endDateControl = this.absenceForm?.get('endDate');
    if (startDateControl?.value && endDateControl?.value && new Date(endDateControl.value) < new Date(startDateControl.value)) {
      return { 'invalidEndDate': true }; // End date cannot be before start date
    }

    return null;
  }
  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files[0];
    this.absenceForm.patchValue({ justificatif: file });
    this.justificatif = file;
  }

  onReasonChange(event: Event): void {
    const selectedReason = (event.target as HTMLSelectElement).value;
    this.showFileUpload = selectedReason === 'SICK_LEAVE';
  }


  onSubmit(): void {
    this.formSubmitted = true;

    if (this.absenceForm.valid) {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data: { message: "Êtes-vous sûr de vouloir enregistrer cette demande d'absence ?" }
      });

      dialogRef.afterClosed().subscribe((confirmed: boolean) => {
        if (confirmed) {
          this.saveAbsenceForm();
        }
      });
    }
  }
  saveAbsenceForm(): void {
    if (!this.tokenStorage.isLoggedIn()) {
      this.errorMessage = 'User is not logged in.';
      return;
    }

    const absenceRequest: DemandeAbsence = {
      dateDebut: this.absenceForm.get('startDate').value,
      dateFin: this.absenceForm.get('endDate').value,
      comment: this.absenceForm.get('comment').value,
      motif: this.absenceForm.get('absenceReason').value
    };

    this.absenceService.addAbsenceRequest(absenceRequest, this.justificatif).subscribe({
      next: (response) => {
        if (response && response.status === 'OK' && response.data) {


          this.successMessage = response.message || 'Votre absence a été ajoutée avec succès';
          this.errorMessage = '';
          this.router.navigate(['/HistoryAbsence']); // Redirect immediately on success
        } else {
          this.errorMessage = response.message || 'Une erreur s\'est produite. Veuillez réessayer.';
          this.successMessage = '';
        }
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Votre absence a échoué. Veuillez vérifier les dates sélectionnées.';
        this.successMessage = '';
        setTimeout(() => this.errorMessage = '', 5000);
      }
    });
  }
  onCancel(): void {
    // Handle cancellation logic here
  }

  getMappedAbsenceType(absenceType: string): string {
    return this.absenceTypeMapping[absenceType] || absenceType;
  }
}
