import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IrakasleHome } from './irakasle-home';

describe('IrakasleHome', () => {
  let component: IrakasleHome;
  let fixture: ComponentFixture<IrakasleHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IrakasleHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IrakasleHome);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
