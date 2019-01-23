import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectionlinksComponent } from './connectionlinks.component';

describe('ConnectionlinksComponent', () => {
  let component: ConnectionlinksComponent;
  let fixture: ComponentFixture<ConnectionlinksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConnectionlinksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectionlinksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
