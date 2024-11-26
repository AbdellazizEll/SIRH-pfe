import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutCompetenceComponent } from './ajout-competence.component';

describe('AjoutCompetenceComponent', () => {
  let component: AjoutCompetenceComponent;
  let fixture: ComponentFixture<AjoutCompetenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AjoutCompetenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AjoutCompetenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
