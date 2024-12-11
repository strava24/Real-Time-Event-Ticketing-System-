import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterOutlet } from '@angular/router';
import { LoginService } from '../../service/loginService/login.service';
import { Login } from '../../model/interface/Credentials';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { Customer } from '../../model/class/Customer';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterOutlet],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  router = inject(Router);
  loginService = inject(LoginService);
  toastrService = inject(ToastrService);

  loginObj: Login = {
    email: '',
    password: ''
  };

  vendorID: number = 0;
  customer: Customer = new Customer();

  /**
   * Method to login the vendor
   */
  onLoginVendor() {
    this.loginService.loginVendor(this.loginObj).subscribe(
      (response) => {
        this.toastrService.success('Logged in Successfully!');
        this.vendorID = response;
        console.log(this.vendorID);

        localStorage.setItem('vendorID', this.vendorID.toString());

        this.router.navigateByUrl('vendor/vendor-dashboard');
      }, error => {
        this.toastrService.error('Invalid Credentials');
      }
    )
  }

  /**
   * Method to login the customer
   */
  onLoginCustomer() {
    this.loginService.loginCustomer(this.loginObj).subscribe(
      (response) => {
        this.toastrService.success('Logged in Successfully!');

        this.customer = response;
        console.log('Customer Account: ' + this.customer);
        localStorage.setItem('customer', JSON.stringify(this.customer));

        this.router.navigateByUrl('customer/events');
      }, error => {
        this.toastrService.error('Invalid Credentials');
      }
    )
  }

  /**
   * Method to redirect to the signup page
   */
  onSingup() {
    this.router.navigateByUrl('signup');
  }

}
