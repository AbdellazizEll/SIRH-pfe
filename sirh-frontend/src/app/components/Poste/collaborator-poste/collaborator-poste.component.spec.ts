import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CollaboratorPosteComponent } from './collaborator-poste.component';

describe('CollaboratorPosteComponent', () => {
  let component: CollaboratorPosteComponent;
  let fixture: ComponentFixture<CollaboratorPosteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CollaboratorPosteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CollaboratorPosteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
