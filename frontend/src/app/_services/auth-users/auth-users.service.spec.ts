import { TestBed } from '@angular/core/testing';

import { AuthUsersService } from './auth-users.service';

describe('AuthUsersService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AuthUsersService = TestBed.get(AuthUsersService);
    expect(service).toBeTruthy();
  });
});
