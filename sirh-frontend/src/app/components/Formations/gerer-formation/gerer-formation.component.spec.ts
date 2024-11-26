import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GererFormationComponent } from './gerer-formation.component';

describe('GererFormationComponent', () => {
  let component: GererFormationComponent;
  let fixture: ComponentFixture<GererFormationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GererFormationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GererFormationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
