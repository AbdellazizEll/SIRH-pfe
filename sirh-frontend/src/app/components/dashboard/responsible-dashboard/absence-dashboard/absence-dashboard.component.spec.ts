import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbsenceDashboardComponent } from './absence-dashboard.component';

describe('AbsenceDashboardComponent', () => {
  let component: AbsenceDashboardComponent;
  let fixture: ComponentFixture<AbsenceDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AbsenceDashboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbsenceDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
