import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuggestionFormationComponent } from './suggestion-formation.component';

describe('SuggestionFormationComponent', () => {
  let component: SuggestionFormationComponent;
  let fixture: ComponentFixture<SuggestionFormationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SuggestionFormationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SuggestionFormationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
