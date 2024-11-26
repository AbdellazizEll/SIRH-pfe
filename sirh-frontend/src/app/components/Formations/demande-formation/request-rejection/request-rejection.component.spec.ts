import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestRejectionComponent } from './request-rejection.component';

describe('RequestRejectionComponent', () => {
  let component: RequestRejectionComponent;
  let fixture: ComponentFixture<RequestRejectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestRejectionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestRejectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
