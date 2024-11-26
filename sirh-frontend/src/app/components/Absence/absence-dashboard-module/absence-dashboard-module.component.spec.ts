import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbsenceDashboardModuleComponent } from './absence-dashboard-module.component';

describe('AbsenceDashboardModuleComponent', () => {
  let component: AbsenceDashboardModuleComponent;
  let fixture: ComponentFixture<AbsenceDashboardModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AbsenceDashboardModuleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbsenceDashboardModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
