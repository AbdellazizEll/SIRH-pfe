import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GererDetailCompetenceComponent } from './gerer-detail-competence.component';

describe('GererDetailCompetenceComponent', () => {
  let component: GererDetailCompetenceComponent;
  let fixture: ComponentFixture<GererDetailCompetenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GererDetailCompetenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GererDetailCompetenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
