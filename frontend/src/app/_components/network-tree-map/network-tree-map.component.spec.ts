import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkTreeMapComponent } from './network-tree-map.component';

describe('NetworkTreeMapComponent', () => {
  let component: NetworkTreeMapComponent;
  let fixture: ComponentFixture<NetworkTreeMapComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NetworkTreeMapComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NetworkTreeMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
