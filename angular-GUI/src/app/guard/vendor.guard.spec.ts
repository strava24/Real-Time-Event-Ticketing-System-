import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { vendorGuard } from './vendor.guard';

describe('vendorGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => vendorGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
