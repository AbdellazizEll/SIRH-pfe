import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyTeamModalComponent } from './modify-team-modal.component';

describe('ModifyTeamModalComponent', () => {
  let component: ModifyTeamModalComponent;
  let fixture: ComponentFixture<ModifyTeamModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifyTeamModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyTeamModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
