import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterOutlet } from '@angular/router';
import { CustomerCredentials, Signup, VendorCredentials } from '../../model/interface/Credentials';
import { LoginService } from '../../service/loginService/login.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterOutlet],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {

  signupObj: Signup = {
    name: '',
    email: '',
    password: ''
  };

  router = inject(Router);
  loginService = inject(LoginService);
  toastrService = inject(ToastrService);

  onLogin() {
    this.router.navigateByUrl('/login')
  }

  onVendorSignup() {

    const vendorCredentialsObj: VendorCredentials = {
      vendorName: this.signupObj.name,
      vendorEmail: this.signupObj.email,
      vendorPassword: this.signupObj.password
    }

    this.loginService.signupVendor(vendorCredentialsObj).subscribe(
      (response) => {
        this.toastrService.success('Sign In was successful!');
        console.log(response);

        localStorage.setItem('vendorID', response.toString());
        this.router.navigateByUrl('vendor/vendor-dashboard');
      }, error => {
        this.toastrService.error('Network Down!');
      }
    )

  }

  onCustomerSignup() {
    const customerCredentialsObj: CustomerCredentials = {
      customerName: this.signupObj.name,
      customerEmail: this.signupObj.email,
      customerPassword: this.signupObj.password
    }

    this.loginService.signupCustomer(customerCredentialsObj).subscribe(
      (response) => {
        this.toastrService.success('Sign In was successful!');
        console.log(response);

        localStorage.setItem('customer', JSON.stringify(response));
        this.router.navigateByUrl('customer/events');
      }, error => {
        this.toastrService.error('Network Down!');
      }
    )

  }

}
