import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCompetenceListComponent } from './view-competence-list.component';

describe('ViewCompetenceListComponent', () => {
  let component: ViewCompetenceListComponent;
  let fixture: ComponentFixture<ViewCompetenceListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewCompetenceListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewCompetenceListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
