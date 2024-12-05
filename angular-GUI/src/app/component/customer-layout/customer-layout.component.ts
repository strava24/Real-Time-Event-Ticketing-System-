import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-customer-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  templateUrl: './customer-layout.component.html',
  styleUrl: './customer-layout.component.css'
})
export class CustomerLayoutComponent {

  router = inject(Router);

  onLogOff() {
    localStorage.removeItem('customer');
    this.router.navigateByUrl('login');
  }

}
