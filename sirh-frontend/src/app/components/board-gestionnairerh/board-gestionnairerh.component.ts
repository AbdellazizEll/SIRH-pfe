import { Component, OnInit } from '@angular/core';
import {CollaboratorService} from "../../_services/collaborator.service";

@Component({
  selector: 'app-board-gestionnairerh',
  templateUrl: './board-gestionnairerh.component.html',
  styleUrls: ['./board-gestionnairerh.component.scss']
})
export class BoardGestionnairerhComponent implements OnInit {

  content!: string;

  constructor(private userService: CollaboratorService) { }

  ngOnInit(): void {
    this.userService.getGestRHBoard().subscribe(
      data => {
        this.content = data;
      },
      err => {
        this.content = JSON.parse(err.error).message;
      }
    );
  }
}
