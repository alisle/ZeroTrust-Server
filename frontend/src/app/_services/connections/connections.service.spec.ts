import { TestBed } from '@angular/core/testing';

import { ConnectionsService } from './connections.service';

describe('ConnectionsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ConnectionsService = TestBed.get(ConnectionsService);
    expect(service).toBeTruthy();
  });
});
