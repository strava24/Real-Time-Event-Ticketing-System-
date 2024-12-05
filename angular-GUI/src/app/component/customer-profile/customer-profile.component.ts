import { Component, inject, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from '../../service/loginService/login.service';
import { Router } from '@angular/router';
import { Customer } from '../../model/class/Customer';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer-profile',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './customer-profile.component.html',
  styleUrl: './customer-profile.component.css'
})
export class CustomerProfileComponent implements OnInit {
  toastrService = inject(ToastrService);
  router = inject(Router);
  loginService = inject(LoginService);

  customerDetails: Customer = new Customer();
  inEditMode: boolean = false;
  customerID: number = 0;

  ngOnInit(): void {
    this.loadCustomerDetails();
  }

  loadCustomerDetails() {
    this.customerID = this.loginService.getCustomerID();

    if (this.customerID === -1) {
      this.toastrService.info('Login to the system!');
      this.router.navigateByUrl('login');
    } else {
      this.loginService.getCustomerDetails(this.customerID).subscribe((response) => {
        this.toastrService.success('Loaded Customer Successfully!');
        this.customerDetails = response;
        console.log(response);
      }, error => {
        this.toastrService.error('Network Down!');
      })
    }

  }

  onEdit() {
    this.inEditMode = true;
  }

  onUpdate() {
    this.loginService.updateCustomer(this.customerDetails).subscribe((response) => {
      if (response) {
        this.toastrService.success('Updated details successfully!');
        console.log(this.customerDetails)
        console.log(response + ' res')
        this.inEditMode = false;
      } else {
        this.toastrService.error('There was an issue!');
      }
    }, error => {
      this.toastrService.error('Network Down!')
    })
  }


}
