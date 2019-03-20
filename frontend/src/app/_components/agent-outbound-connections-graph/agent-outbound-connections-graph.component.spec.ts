import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentOutboundConnectionsGraphComponent } from './agent-outbound-connections-graph.component';

describe('AgentOutboundConnectionsGraphComponent', () => {
  let component: AgentOutboundConnectionsGraphComponent;
  let fixture: ComponentFixture<AgentOutboundConnectionsGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgentOutboundConnectionsGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgentOutboundConnectionsGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
