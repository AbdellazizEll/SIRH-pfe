import { Component, OnInit } from '@angular/core';
import {CollaboratorService} from "../../_services/collaborator.service";

@Component({
  selector: 'app-board-manager',
  templateUrl: './board-manager.component.html',
  styleUrls: ['./board-manager.component.scss']
})
export class BoardManagerComponent implements OnInit {


  content!: string;

  constructor(private userService : CollaboratorService) { }

  ngOnInit(): void {

      this.userService.getManagerBoard().subscribe(
        data => {
          this.content = data;
        },
        err => {
          this.content = JSON.parse(err.error).message;
        }
      );
    }



}
