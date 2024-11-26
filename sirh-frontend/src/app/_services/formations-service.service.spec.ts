import { TestBed } from '@angular/core/testing';

import { FormationsServiceService } from './formations-service.service';

describe('FormationsServiceService', () => {
  let service: FormationsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormationsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
