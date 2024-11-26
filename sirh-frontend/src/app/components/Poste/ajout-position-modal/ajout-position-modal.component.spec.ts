import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutPositionModalComponent } from './ajout-position-modal.component';

describe('AjoutPositionModalComponent', () => {
  let component: AjoutPositionModalComponent;
  let fixture: ComponentFixture<AjoutPositionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AjoutPositionModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AjoutPositionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
