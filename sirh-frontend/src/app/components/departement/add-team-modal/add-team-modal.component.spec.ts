import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTeamModalComponent } from './add-team-modal.component';

describe('AddTeamModalComponent', () => {
  let component: AddTeamModalComponent;
  let fixture: ComponentFixture<AddTeamModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddTeamModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddTeamModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
