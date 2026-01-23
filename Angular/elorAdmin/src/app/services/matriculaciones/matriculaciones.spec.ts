import { TestBed } from '@angular/core/testing';

import { Matriculaciones } from './matriculaciones';

describe('Matriculaciones', () => {
  let service: Matriculaciones;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Matriculaciones);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
