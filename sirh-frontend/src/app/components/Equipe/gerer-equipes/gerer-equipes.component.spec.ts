import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GererEquipesComponent } from './gerer-equipes.component';

describe('GererEquipesComponent', () => {
  let component: GererEquipesComponent;
  let fixture: ComponentFixture<GererEquipesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GererEquipesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GererEquipesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
