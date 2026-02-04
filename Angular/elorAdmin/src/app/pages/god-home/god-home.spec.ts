import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GodHome } from './god-home';

describe('GodHome', () => {
  let component: GodHome;
  let fixture: ComponentFixture<GodHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GodHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GodHome);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
