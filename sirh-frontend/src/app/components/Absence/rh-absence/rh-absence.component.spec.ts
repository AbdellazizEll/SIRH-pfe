import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RhAbsenceComponent } from './rh-absence.component';

describe('RhAbsenceComponent', () => {
  let component: RhAbsenceComponent;
  let fixture: ComponentFixture<RhAbsenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RhAbsenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RhAbsenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
