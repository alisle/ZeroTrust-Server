import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectionLinksComponent } from './connection-links.component';

describe('ConnectionLinksComponent', () => {
  let component: ConnectionLinksComponent;
  let fixture: ComponentFixture<ConnectionLinksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConnectionLinksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectionLinksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
