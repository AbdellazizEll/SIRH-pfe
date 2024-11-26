import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyPosteComponent } from './modify-poste.component';

describe('ModifyPosteComponent', () => {
  let component: ModifyPosteComponent;
  let fixture: ComponentFixture<ModifyPosteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifyPosteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifyPosteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
