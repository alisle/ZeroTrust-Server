import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveConnectionsGraphComponent } from './active-connections-graph.component';

describe('ActiveConnectionsGraphComponent', () => {
  let component: ActiveConnectionsGraphComponent;
  let fixture: ComponentFixture<ActiveConnectionsGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ActiveConnectionsGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiveConnectionsGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
