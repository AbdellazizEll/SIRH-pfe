import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifierCompetenceComponent } from './modifier-competence.component';

describe('ModifierCompetenceComponent', () => {
  let component: ModifierCompetenceComponent;
  let fixture: ComponentFixture<ModifierCompetenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifierCompetenceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifierCompetenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
