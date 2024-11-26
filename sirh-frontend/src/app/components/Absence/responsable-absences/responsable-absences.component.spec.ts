import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponsableAbsencesComponent } from './responsable-absences.component';

describe('ResponsableAbsencesComponent', () => {
  let component: ResponsableAbsencesComponent;
  let fixture: ComponentFixture<ResponsableAbsencesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResponsableAbsencesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponsableAbsencesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
