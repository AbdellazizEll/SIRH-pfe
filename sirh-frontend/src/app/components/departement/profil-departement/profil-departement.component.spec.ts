import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilDepartementComponent } from './profil-departement.component';

describe('ProfilDepartementComponent', () => {
  let component: ProfilDepartementComponent;
  let fixture: ComponentFixture<ProfilDepartementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfilDepartementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfilDepartementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
