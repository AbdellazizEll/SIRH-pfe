import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignCollabComponent } from './assign-collab.component';

describe('AssignCollabComponent', () => {
  let component: AssignCollabComponent;
  let fixture: ComponentFixture<AssignCollabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssignCollabComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignCollabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
