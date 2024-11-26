import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GererDepartementComponent } from './gerer-departement.component';

describe('GererDepartementComponent', () => {
  let component: GererDepartementComponent;
  let fixture: ComponentFixture<GererDepartementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GererDepartementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GererDepartementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
