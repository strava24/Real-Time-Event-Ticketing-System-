import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { VendorDashboardComponent } from './component/vendor-dashboard/vendor-dashboard.component';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ // Import animations module
    RouterOutlet  // For routing if needed
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular-GUI';
}
