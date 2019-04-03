import { TestBed } from '@angular/core/testing';

import { NetworksService } from './networks.service';

describe('NetworksService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NetworksService = TestBed.get(NetworksService);
    expect(service).toBeTruthy();
  });
});
