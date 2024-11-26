import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardGestionnairerhComponent } from './board-gestionnairerh.component';

describe('BoardGestionnairerhComponent', () => {
  let component: BoardGestionnairerhComponent;
  let fixture: ComponentFixture<BoardGestionnairerhComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoardGestionnairerhComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoardGestionnairerhComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
