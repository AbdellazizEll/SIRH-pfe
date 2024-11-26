import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormationDashboardModuleComponent } from './formation-dashboard-module.component';

describe('FormationDashboardModuleComponent', () => {
  let component: FormationDashboardModuleComponent;
  let fixture: ComponentFixture<FormationDashboardModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormationDashboardModuleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormationDashboardModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
