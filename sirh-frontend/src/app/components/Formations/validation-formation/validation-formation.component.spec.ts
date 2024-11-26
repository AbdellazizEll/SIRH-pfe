import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationFormationComponent } from './validation-formation.component';

describe('ValidationFormationComponent', () => {
  let component: ValidationFormationComponent;
  let fixture: ComponentFixture<ValidationFormationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ValidationFormationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ValidationFormationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
