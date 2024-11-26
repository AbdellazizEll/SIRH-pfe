import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamEnrollmentsComponent } from './team-enrollments.component';

describe('TeamEnrollmentsComponent', () => {
  let component: TeamEnrollmentsComponent;
  let fixture: ComponentFixture<TeamEnrollmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeamEnrollmentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamEnrollmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
