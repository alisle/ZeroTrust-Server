import { TestBed } from '@angular/core/testing';

import { ConnectionlinkService } from './connectionlink.service';

describe('ConnectionlinkService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ConnectionlinkService = TestBed.get(ConnectionlinkService);
    expect(service).toBeTruthy();
  });
});
