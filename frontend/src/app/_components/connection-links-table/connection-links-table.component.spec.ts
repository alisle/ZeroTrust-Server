import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectionLinksTableComponent } from './connection-links-table.component';

describe('ConnectionLinksTableComponent', () => {
  let component: ConnectionLinksTableComponent;
  let fixture: ComponentFixture<ConnectionLinksTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConnectionLinksTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectionLinksTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
