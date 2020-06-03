import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuyBooksComponent } from './buy-books.component';

describe('BuyBooksComponent', () => {
  let component: BuyBooksComponent;
  let fixture: ComponentFixture<BuyBooksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuyBooksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuyBooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
