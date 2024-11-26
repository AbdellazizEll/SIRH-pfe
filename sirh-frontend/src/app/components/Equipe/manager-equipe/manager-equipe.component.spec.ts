import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerEquipeComponent } from './manager-equipe.component';

describe('ManagerEquipeComponent', () => {
  let component: ManagerEquipeComponent;
  let fixture: ComponentFixture<ManagerEquipeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManagerEquipeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagerEquipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
