import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from '../../service/loginService/login.service';
import { Vendor } from '../../model/class/Vendor';
import { FormsModule } from '@angular/forms';
import { EventService } from '../../service/eventService/event.service';

@Component({
  selector: 'app-vendor-profile',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './vendor-profile.component.html',
  styleUrl: './vendor-profile.component.css'
})
export class VendorProfileComponent implements OnInit {

  toastrService = inject(ToastrService);
  router = inject(Router);
  loginService = inject(LoginService);
  eventService = inject(EventService);
  vendorDetails: Vendor = new Vendor();

  inEditMode: boolean = false;

  vendorID: number = 0;

  eventCountByVendor: number = 0;

  ngOnInit(): void {
    this.loadVendorDetails();
    this.getEventCount();
  }

  /**
   * Method to load the vendor details
   */
  loadVendorDetails() {

    this.vendorID = this.loginService.getVendorID();

    if (this.vendorID === -1) {
      this.toastrService.info('Login to the system!');
      this.router.navigateByUrl('login');
    } else {
      this.loginService.getVendorDetails(this.vendorID).subscribe((response) => {
        this.toastrService.success('Loaded Vendor Details!');
        this.vendorDetails = response;
        console.log(this.vendorDetails)
      }, error => {
        this.toastrService.error('Network Down!');
      })
    }
  }

  onEdit() {
    this.inEditMode = true;
  }

  /**
   * Method to update the vendor details
   */
  onUpdate() {
    this.loginService.updateVendor(this.vendorDetails).subscribe((response) => {
      if (response) {
        this.toastrService.success('Updated details successfully!');
        this.inEditMode = false;
      } else {
        this.toastrService.error('There was an issue!');
      }
    }, error => {
      this.toastrService.error('Network Down!')
    })
  }

  /**
   * Method to get the event count of the vendor
   */
  getEventCount() {
    this.eventService.getEventCoustByEvndorID(this.vendorID).subscribe((response) => {
      this.eventCountByVendor = response;
      console.log('events hosted By vendor :' + this.eventCountByVendor);
    }, error => {
      console.log('There was an error getting the count!')
    })
  }

}
