import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CompetenceService } from '../../../_services/competence.service';
import {Key} from "@ng-bootstrap/ng-bootstrap/util/key";
import {Router} from "@angular/router";

@Component({
  selector: 'app-ajout-competence',
  templateUrl: './ajout-competence.component.html',
  styleUrls: ['./ajout-competence.component.scss']
})
export class AjoutCompetenceComponent implements OnInit {
  private typeScale: { [key: string]: string } = {
    'DESCRIPTIF': 'Description',
    'STARS': 'Etoiles',
    'NUMERIC': 'Numerique'
  };

  competenceForm: FormGroup;
  scaleTypes: { key: string, display: string }[] = [];

  successMessage: string;
  errorMessage: string;

  constructor(
    private fb: FormBuilder,
    private competenceService: CompetenceService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.competenceForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      scaleType: ['', [Validators.required]]
    });

    this.fetchScaleTypes();
  }

  submitForm() {
    if (this.competenceForm.valid) {
      console.log(this.competenceForm.value);
      this.competenceService.createCompetence(this.competenceForm.value).subscribe(
        (response) => {
          this.successMessage = 'Compétence a été ajouté avec succés';
          this.errorMessage = '';
          setTimeout(() => this.successMessage = '', 4000);
          this.reloadPage();
          console.log(response);
        },
        (error) => {
          this.errorMessage = "Erreur d'ajout d'un compétence ";
          this.successMessage = '';
          setTimeout(() => this.errorMessage = '', 4000);
        }
      );
    }
  }

  getMappedTypeScale(typeScale: string): string {
    return this.typeScale[typeScale] || typeScale;
  }

  fetchScaleTypes(): void {
    this.competenceService.getScaleTypes().subscribe(
      (types) => {
        this.scaleTypes = Object.keys(types).map(key => ({
          key,
          display: types[key]
        }));
      },
      (error) => this.errorMessage = 'Error fetching scale types'
    );
  }

  private reloadPage() {
    setTimeout(() =>  4000);
    window.location.reload();
  }

  onCancel() {
    this.router.navigate(["GererDetailCompetence"])
  }
}
