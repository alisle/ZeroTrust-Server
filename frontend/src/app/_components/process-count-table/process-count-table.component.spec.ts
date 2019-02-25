import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessCountTableComponent } from './process-count-table.component';

describe('ProcessCountTableComponent', () => {
  let component: ProcessCountTableComponent;
  let fixture: ComponentFixture<ProcessCountTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProcessCountTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessCountTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
