import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateProgressModalComponent } from './update-progress-modal.component';

describe('UpdateProgressModalComponent', () => {
  let component: UpdateProgressModalComponent;
  let fixture: ComponentFixture<UpdateProgressModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateProgressModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateProgressModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
