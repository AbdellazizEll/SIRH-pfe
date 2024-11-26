import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Inject, Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-modifier-competence',
  templateUrl: './modifier-competence.component.html',
  styleUrls: ['./modifier-competence.component.scss']
})
export class ModifierCompetenceComponent implements OnInit {
  competenceForm: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<ModifierCompetenceComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,  // Receive the competence data
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // Initialize the form with data from the competence
    this.competenceForm = this.fb.group({
      name: [this.data.name, Validators.required],
      description: [this.data.description, Validators.required],
      scaleType: [this.data.scaleType, Validators.required]
    });
  }

  saveChanges(): void {
    // Here, you would typically call a service to save the updated competence
    // For now, close the dialog and pass a signal back to the parent component
    this.dialogRef.close('updated');
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
