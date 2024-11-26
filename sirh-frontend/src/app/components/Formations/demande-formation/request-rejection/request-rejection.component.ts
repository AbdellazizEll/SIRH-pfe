import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-request-rejection',
  templateUrl: './request-rejection.component.html',
  styleUrls: ['./request-rejection.component.scss']
})
export class RequestRejectionComponent  {

  rejectionReason: string = '';

  constructor(
    public dialogRef: MatDialogRef<RequestRejectionComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  // Function to close the dialog
  closeDialog(): void {
    this.dialogRef.close();
  }

  // Function to confirm rejection and return the reason
  confirmRejection(): void {
    if (this.rejectionReason.trim()) {
      this.dialogRef.close({ rejectionReason: this.rejectionReason });
    } else {
      alert('Veuillez fournir une raison pour le rejet.');
    }
  }

}
