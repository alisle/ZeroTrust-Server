import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkAddDialog } from './network-add.dialog';

describe('NetworkAddDialog', () => {
  let component: NetworkAddDialog;
  let fixture: ComponentFixture<NetworkAddDialog>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkAddDialog ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkAddDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
