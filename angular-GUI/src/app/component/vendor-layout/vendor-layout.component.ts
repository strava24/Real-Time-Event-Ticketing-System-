import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-vendor-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './vendor-layout.component.html',
  styleUrl: './vendor-layout.component.css'
})
export class VendorLayoutComponent {

  router = inject(Router);

  /**
   * Method to sign out a vendor
   */
  onLogOff() {
    localStorage.removeItem('vendorID');
    this.router.navigateByUrl('login');
  }

}
