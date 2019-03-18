import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentInboundConnectionsGraphComponent } from './agent-inbound-connections-graph.component';

describe('AgentInboundConnectionsGraphComponent', () => {
  let component: AgentInboundConnectionsGraphComponent;
  let fixture: ComponentFixture<AgentInboundConnectionsGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgentInboundConnectionsGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgentInboundConnectionsGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
