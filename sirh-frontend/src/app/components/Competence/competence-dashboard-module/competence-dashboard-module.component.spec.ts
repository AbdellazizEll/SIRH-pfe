import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetenceDashboardModuleComponent } from './competence-dashboard-module.component';

describe('CompetenceDashboardModuleComponent', () => {
  let component: CompetenceDashboardModuleComponent;
  let fixture: ComponentFixture<CompetenceDashboardModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompetenceDashboardModuleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompetenceDashboardModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
