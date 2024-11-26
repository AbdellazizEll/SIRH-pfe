import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutPosteComponent } from './ajout-poste.component';

describe('AjoutPosteComponent', () => {
  let component: AjoutPosteComponent;
  let fixture: ComponentFixture<AjoutPosteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AjoutPosteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AjoutPosteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
