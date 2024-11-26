import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-validation-formation',
  templateUrl: './validation-formation.component.html',
  styleUrls: ['./validation-formation.component.scss']
})
export class ValidationFormationComponent implements OnInit {
  requests: any[];

  constructor() { }

  ngOnInit(): void {
  }

  approveRequest(request: any) {

  }

  rejectRequest(request: any) {

  }
}
