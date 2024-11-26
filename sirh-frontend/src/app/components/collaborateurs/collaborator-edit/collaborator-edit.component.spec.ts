import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CollaboratorEditComponent } from './collaborator-edit.component';

describe('CollaboratorEditComponent', () => {
  let component: CollaboratorEditComponent;
  let fixture: ComponentFixture<CollaboratorEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CollaboratorEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CollaboratorEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
