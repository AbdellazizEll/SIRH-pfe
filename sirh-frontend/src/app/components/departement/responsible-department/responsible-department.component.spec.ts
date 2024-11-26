import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponsibleDepartmentComponent } from './responsible-department.component';

describe('ResponsibleDepartmentComponent', () => {
  let component: ResponsibleDepartmentComponent;
  let fixture: ComponentFixture<ResponsibleDepartmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResponsibleDepartmentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponsibleDepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
