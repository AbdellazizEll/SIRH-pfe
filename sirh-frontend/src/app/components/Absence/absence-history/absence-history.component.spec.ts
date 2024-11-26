import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbsenceHistoryComponent } from './absence-history.component';

describe('AbsenceHistoryComponent', () => {
  let component: AbsenceHistoryComponent;
  let fixture: ComponentFixture<AbsenceHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AbsenceHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbsenceHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
