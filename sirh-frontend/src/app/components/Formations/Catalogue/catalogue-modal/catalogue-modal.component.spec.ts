import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogueModalComponent } from './catalogue-modal.component';

describe('CatalogueModalComponent', () => {
  let component: CatalogueModalComponent;
  let fixture: ComponentFixture<CatalogueModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CatalogueModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CatalogueModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
