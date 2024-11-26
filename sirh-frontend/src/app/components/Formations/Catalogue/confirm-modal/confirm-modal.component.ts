import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.scss']
})
export class ConfirmModalComponent  {
  @Input() message: string = 'Êtes-vous sûr de vouloir effectuer cette action ?';
  @Input() confirmButtonText: string = 'Confirmer';  // Default value
  @Input() cancelButtonText: string = 'Annuler';  // Default value

  constructor(public activeModal: NgbActiveModal) {}

  confirm() {
    this.activeModal.close(true); // Resolve the modal with true (confirmed)
  }

  cancel() {
    this.activeModal.dismiss(false); // Dismiss the modal with false (cancelled)
  }
}
