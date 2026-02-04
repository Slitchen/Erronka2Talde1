import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CentroDetails } from './centro-details';

describe('CentroDetails', () => {
  let component: CentroDetails;
  let fixture: ComponentFixture<CentroDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CentroDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CentroDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
