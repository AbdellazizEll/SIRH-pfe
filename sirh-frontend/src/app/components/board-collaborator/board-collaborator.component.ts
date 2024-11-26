import { Component, OnInit } from '@angular/core';
import {CollaboratorService} from "../../_services/collaborator.service";

@Component({
  selector: 'app-board-collaborator',
  templateUrl: './board-collaborator.component.html',
  styleUrls: ['./board-collaborator.component.scss']
})
export class BoardCollaboratorComponent implements OnInit {

  content!: string;

  constructor(private userService: CollaboratorService) { }

  ngOnInit(): void {
    this.userService.getPublicContent().subscribe(
      data => {
        this.content = data;
      },
      err => {
        this.content = JSON.parse(err.error).message;
      }
    );
  }

}
