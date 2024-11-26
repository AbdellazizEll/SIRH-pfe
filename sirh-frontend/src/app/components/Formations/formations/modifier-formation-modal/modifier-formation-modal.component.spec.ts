import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifierFormationModalComponent } from './modifier-formation-modal.component';

describe('ModifierFormationModalComponent', () => {
  let component: ModifierFormationModalComponent;
  let fixture: ComponentFixture<ModifierFormationModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifierFormationModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifierFormationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
