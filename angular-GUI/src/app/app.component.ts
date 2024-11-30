import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { VendorDashboardComponent } from './component/vendor-dashboard/vendor-dashboard.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, VendorDashboardComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular-GUI';
}
