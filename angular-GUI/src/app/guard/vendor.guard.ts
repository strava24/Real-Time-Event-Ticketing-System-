import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const vendorGuard: CanActivateFn = (route, state) => {

  const vendor = localStorage.getItem('vendorID');
  const router = inject(Router)

  if (vendor != null) {
    return true
  } else {
    router.navigateByUrl('/login')
    return false
  }

};
