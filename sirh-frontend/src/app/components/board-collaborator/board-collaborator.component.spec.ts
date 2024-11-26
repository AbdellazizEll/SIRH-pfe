import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardCollaboratorComponent } from './board-collaborator.component';

describe('BoardCollaboratorComponent', () => {
  let component: BoardCollaboratorComponent;
  let fixture: ComponentFixture<BoardCollaboratorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoardCollaboratorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoardCollaboratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
