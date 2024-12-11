import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

// Guard to make sure that the vendor is logged in to access the application
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
