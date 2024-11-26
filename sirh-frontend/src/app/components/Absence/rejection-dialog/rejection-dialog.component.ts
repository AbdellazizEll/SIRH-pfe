import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-rejection-dialog',
  templateUrl: './rejection-dialog.component.html',
  styleUrls: ['./rejection-dialog.component.scss']
})
export class RejectionDialogComponent  {

  rejectionReason: string = '';

  constructor(
    public dialogRef: MatDialogRef<RejectionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onConfirm(): void {
    if (this.rejectionReason.trim()) {
      this.dialogRef.close(this.rejectionReason); // Pass the reason back to the calling component
    } else {
      alert("Veuillez fournir une raison pour le refus.");
    }
  }

  onCancel(): void {
    this.dialogRef.close(null); // Close without a reason
  }

}
