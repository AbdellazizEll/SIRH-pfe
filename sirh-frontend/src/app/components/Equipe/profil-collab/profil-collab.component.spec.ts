import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilCollabComponent } from './profil-collab.component';

describe('ProfilCollabComponent', () => {
  let component: ProfilCollabComponent;
  let fixture: ComponentFixture<ProfilCollabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfilCollabComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfilCollabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
