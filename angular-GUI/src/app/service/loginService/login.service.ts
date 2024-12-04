import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Constant } from '../../constant/Constant';
import { Login } from '../../model/interface/Credentials';
import { Router } from '@angular/router';
import { Customer } from '../../model/class/Customer';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient, private router: Router) { }


  getVendorID(): number {

    const data: string | null = localStorage.getItem('vendorID');

    if (data === null) {
      this.router.navigateByUrl('/login');
      return -1;
    } else {
      const vendorID = parseInt(data, 10);
      if (isNaN(vendorID)) {
        console.error('Invalid vendor ID');
        this.router.navigateByUrl('/login');
        return -1;
      } else {
        console.log('Vendor ID:', vendorID);

        return vendorID;

      }
    }
  }

  loginVendor(loginObj: Login) {

    let params = new HttpParams()
      .set('email', loginObj.email)
      .set('password', loginObj.password)

    return this.http.post<number>(environment.API_URL + Constant.VENDOR_METHOD.LOGIN_VENDOR, {}, { params });
  }

  loginCustomer(loginObj: Login) {
    let params = new HttpParams()
      .set('email', loginObj.email)
      .set('password', loginObj.password)

    return this.http.post<Customer>(environment.API_URL + Constant.CUSTOMER_METHOD.LOGIN_CUSTOMER, {}, { params });
  }



}
