import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const customerGuard: CanActivateFn = (route, state) => {
  const customerDataString = localStorage.getItem('customer');
  const router = inject(Router);

  console.log(customerDataString)

  if (customerDataString) {
    return true;
  } else {
    console.log('No customer data found in localStorage');
    router.navigateByUrl('/login');
    return false;
  }

};
