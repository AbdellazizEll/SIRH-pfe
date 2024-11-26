import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifierEquipeModalComponent } from './modifier-equipe-modal.component';

describe('ModifierEquipeModalComponent', () => {
  let component: ModifierEquipeModalComponent;
  let fixture: ComponentFixture<ModifierEquipeModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifierEquipeModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifierEquipeModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
