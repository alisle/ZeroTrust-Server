import { TestBed } from '@angular/core/testing';

import { ConnectionLinkService } from './connection-link.service';

describe('ConnectionLinkService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ConnectionLinkService = TestBed.get(ConnectionLinkService);
    expect(service).toBeTruthy();
  });
});
