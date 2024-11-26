import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GererCatalogueComponent } from './gerer-catalogue.component';

describe('GererCatalogueComponent', () => {
  let component: GererCatalogueComponent;
  let fixture: ComponentFixture<GererCatalogueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GererCatalogueComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GererCatalogueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
