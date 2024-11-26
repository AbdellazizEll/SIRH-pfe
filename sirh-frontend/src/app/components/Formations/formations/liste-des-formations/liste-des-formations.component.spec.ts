import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeDesFormationsComponent } from './liste-des-formations.component';

describe('ListeDesFormationsComponent', () => {
  let component: ListeDesFormationsComponent;
  let fixture: ComponentFixture<ListeDesFormationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListeDesFormationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListeDesFormationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
