import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCountTableComponent } from './user-count-table.component';

describe('UserCountTableComponent', () => {
  let component: UserCountTableComponent;
  let fixture: ComponentFixture<UserCountTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserCountTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCountTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
