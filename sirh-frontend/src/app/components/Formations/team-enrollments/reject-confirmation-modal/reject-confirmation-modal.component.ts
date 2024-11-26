import { Component, Input } from '@angular/core';
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-reject-confirmation-modal',
  templateUrl: './reject-confirmation-modal.component.html',
  styleUrls: ['./reject-confirmation-modal.component.scss']
})
export class RejectConfirmationModalComponent {
  @Input() message: string = 'Are you sure you want to proceed?';
  @Input() confirmButtonText: string = 'Confirm';
  @Input() cancelButtonText: string = 'Cancel';
  @Input() isRejection: boolean = false; // Ensure this input is set correctly
  rejectionReason: string = '';

  constructor(public activeModal: NgbActiveModal) {}

  confirm() {
    const result = this.isRejection ? this.rejectionReason : true;
    this.activeModal.close(result);
  }

  cancel() {
    this.activeModal.dismiss(false);
  }
}
