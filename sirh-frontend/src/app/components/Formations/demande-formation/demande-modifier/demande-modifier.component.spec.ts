import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandeModifierComponent } from './demande-modifier.component';

describe('DemandeModifierComponent', () => {
  let component: DemandeModifierComponent;
  let fixture: ComponentFixture<DemandeModifierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemandeModifierComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DemandeModifierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
