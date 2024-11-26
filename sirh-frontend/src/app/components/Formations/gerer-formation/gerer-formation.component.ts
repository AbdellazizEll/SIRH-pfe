import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-gerer-formation',
  templateUrl: './gerer-formation.component.html',
  styleUrls: ['./gerer-formation.component.scss']
})
export class GererFormationComponent implements OnInit {
  formations: any;

  constructor() { }

  ngOnInit(): void {
  }

  editFormation(formation: any) {
    
  }

  deleteFormation(formation: any) {
    
  }
}
