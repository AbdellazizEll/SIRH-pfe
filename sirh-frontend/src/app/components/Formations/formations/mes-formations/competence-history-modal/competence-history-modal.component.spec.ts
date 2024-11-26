import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetenceHistoryModalComponent } from './competence-history-modal.component';

describe('CompetenceHistoryModalComponent', () => {
  let component: CompetenceHistoryModalComponent;
  let fixture: ComponentFixture<CompetenceHistoryModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompetenceHistoryModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompetenceHistoryModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
