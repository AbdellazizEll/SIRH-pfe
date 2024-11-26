import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CollabDashboardModuleComponent } from './collab-dashboard-module.component';

describe('CollabDashboardModuleComponent', () => {
  let component: CollabDashboardModuleComponent;
  let fixture: ComponentFixture<CollabDashboardModuleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CollabDashboardModuleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CollabDashboardModuleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
