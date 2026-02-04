import { TestBed } from '@angular/core/testing';

import { Ciclos } from './ciclos';

describe('Ciclos', () => {
  let service: Ciclos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Ciclos);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
