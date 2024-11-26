import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-competence-history-modal',
  templateUrl: './competence-history-modal.component.html',
})
export class CompetenceHistoryModalComponent {
  @Input() competenceHistory: any[] = [];
  @Input() historySuccessMessage: string = '';
  @Input() historyErrorMessage: string = '';

  constructor(public activeModal: NgbActiveModal) {}

  closeModal() {
    this.activeModal.dismiss();
  }
}
