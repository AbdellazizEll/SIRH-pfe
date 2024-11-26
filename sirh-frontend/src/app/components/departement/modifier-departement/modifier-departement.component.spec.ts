import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifierDepartementComponent } from './modifier-departement.component';

describe('ModifierDepartementComponent', () => {
  let component: ModifierDepartementComponent;
  let fixture: ComponentFixture<ModifierDepartementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifierDepartementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifierDepartementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
