import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBookStoreComponent } from './my-book-store.component';

describe('MyBookStoreComponent', () => {
  let component: MyBookStoreComponent;
  let fixture: ComponentFixture<MyBookStoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyBookStoreComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyBookStoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
