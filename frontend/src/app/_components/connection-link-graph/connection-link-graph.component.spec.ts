import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectionLinkGraphComponent } from './connection-link-graph.component';

describe('ConnectionLinkGraphComponent', () => {
  let component: ConnectionLinkGraphComponent;
  let fixture: ComponentFixture<ConnectionLinkGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConnectionLinkGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectionLinkGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
