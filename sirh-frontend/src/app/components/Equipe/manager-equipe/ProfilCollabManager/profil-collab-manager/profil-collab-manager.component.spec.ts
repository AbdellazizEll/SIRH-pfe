import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilCollabManagerComponent } from './profil-collab-manager.component';

describe('ProfilCollabManagerComponent', () => {
  let component: ProfilCollabManagerComponent;
  let fixture: ComponentFixture<ProfilCollabManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfilCollabManagerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfilCollabManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
