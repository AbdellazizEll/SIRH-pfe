import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCollaboratorsModalComponent } from './add-collaborators-modal.component';

describe('AddCollaboratorsModalComponent', () => {
  let component: AddCollaboratorsModalComponent;
  let fixture: ComponentFixture<AddCollaboratorsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddCollaboratorsModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCollaboratorsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
