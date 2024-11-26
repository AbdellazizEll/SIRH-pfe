import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectConfirmationModalComponent } from './reject-confirmation-modal.component';

describe('RejectConfirmationModalComponent', () => {
  let component: RejectConfirmationModalComponent;
  let fixture: ComponentFixture<RejectConfirmationModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectConfirmationModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RejectConfirmationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
