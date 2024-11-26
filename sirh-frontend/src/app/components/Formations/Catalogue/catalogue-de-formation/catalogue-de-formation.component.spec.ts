import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogueDeFormationComponent } from './catalogue-de-formation.component';

describe('CatalogueDeFormationComponent', () => {
  let component: CatalogueDeFormationComponent;
  let fixture: ComponentFixture<CatalogueDeFormationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CatalogueDeFormationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CatalogueDeFormationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
