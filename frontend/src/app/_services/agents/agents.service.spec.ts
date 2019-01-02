import { TestBed } from '@angular/core/testing';

import { AgentsService } from './agents.service';

describe('AgentsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AgentsService = TestBed.get(AgentsService);
    expect(service).toBeTruthy();
  });
});
