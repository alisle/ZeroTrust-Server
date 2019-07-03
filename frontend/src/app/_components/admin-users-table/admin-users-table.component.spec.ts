import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUsersTableComponent } from './admin-users-table.component';

describe('AdminUsersTableComponent', () => {
  let component: AdminUsersTableComponent;
  let fixture: ComponentFixture<AdminUsersTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminUsersTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUsersTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
