import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUsersAddDialog } from './admin-users-add.dialog';

describe('AdminUsersAddDialog', () => {
  let component: AdminUsersAddDialog;
  let fixture: ComponentFixture<AdminUsersAddDialog>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminUsersAddDialog ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminUsersAddDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
