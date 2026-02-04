import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IkasleHome } from './ikasle-home';

describe('IkasleHome', () => {
  let component: IkasleHome;
  let fixture: ComponentFixture<IkasleHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IkasleHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IkasleHome);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
