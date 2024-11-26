import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {EnrollmentServiceService} from "../../../../_services/enrollment-service.service";

@Component({
  selector: 'app-update-progress-modal',
  templateUrl: './update-progress-modal.component.html',
  styleUrls: ['./update-progress-modal.component.scss']
})
export class UpdateProgressModalComponent  {


  @Input() collaboratorId!: number;
  @Input() formationId!: number;
  progress: number = 0;
  errorMessage: string = '';

  constructor(public activeModal: NgbActiveModal, private enrollmentService: EnrollmentServiceService) {}

  // Method to update progress via the service
  updateProgress(): void {
    if (this.progress < 0 || this.progress > 100) {
      this.errorMessage = "La progression doit être comprise entre 0 et 100.";
      return;
    }

    // Update progress for the training
    this.enrollmentService.updateProgressForTraining(this.collaboratorId, this.formationId, this.progress)
      .subscribe(
        () => {
          this.activeModal.close(this.progress);  // Close modal with updated progress
        },
        (error) => {
          this.errorMessage = "Erreur lors de la mise à jour de la progression.";
          console.error(error);
        }
      );
  }

  // Close modal without updating
  closeModal(): void {
    this.activeModal.dismiss();
  }

}
