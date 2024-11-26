import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PositionDetailModalComponent } from './position-detail-modal.component';

describe('PositionDetailModalComponent', () => {
  let component: PositionDetailModalComponent;
  let fixture: ComponentFixture<PositionDetailModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PositionDetailModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PositionDetailModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
