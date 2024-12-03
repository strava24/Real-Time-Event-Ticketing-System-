import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../service/loginService/login.service';
import { Login } from '../../model/interface/Credentials';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
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

  onLogin() {
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

  onSingup() {
    this.router.navigateByUrl('/signup');
  }

}
