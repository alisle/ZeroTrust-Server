import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectionLinkDetailsComponent } from './connection-link-details.component';

describe('ConnectionLinkDetailsComponent', () => {
  let component: ConnectionLinkDetailsComponent;
  let fixture: ComponentFixture<ConnectionLinkDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConnectionLinkDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectionLinkDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
