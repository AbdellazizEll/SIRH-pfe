import { TestBed } from '@angular/core/testing';

import { DemandeFormationServiceService } from './demande-formation-service.service';

describe('DemandeFormationServiceService', () => {
  let service: DemandeFormationServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DemandeFormationServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
