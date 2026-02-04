import { TestBed } from '@angular/core/testing';

import { Modulos } from './modulos';

describe('Modulos', () => {
  let service: Modulos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Modulos);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
