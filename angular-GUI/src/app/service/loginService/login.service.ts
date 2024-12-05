import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Constant } from '../../constant/Constant';
import { CustomerCredentials, Login, VendorCredentials } from '../../model/interface/Credentials';
import { Router } from '@angular/router';
import { Customer } from '../../model/class/Customer';
import { Vendor } from '../../model/class/Vendor';
import { retryWhen } from 'rxjs';

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

  getCustomerID() {
    const data: string | null = localStorage.getItem('customer');

    if (data === null) {
      this.router.navigateByUrl('/login');
      return -1;
    } else {
      const customer: Customer = JSON.parse(data);
      return customer.customerID;
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

  signupVendor(vendor: VendorCredentials) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<number>(environment.API_URL + Constant.VENDOR_METHOD.SIGNUP_VENDOR, vendor, { headers });
  }

  signupCustomer(customer: CustomerCredentials) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<Customer>(environment.API_URL + Constant.CUSTOMER_METHOD.SIGNUP_CUSTOMER, customer, { headers });
  }

  getVendorDetails(vendorID: number) {
    return this.http.get<Vendor>(environment.API_URL + Constant.VENDOR_METHOD.GET_VENDOR(vendorID));
  }

  updateVendor(vendor: Vendor) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.put<boolean>(environment.API_URL + Constant.VENDOR_METHOD.UPDATE_VENDOR(vendor.vendorID), vendor, { headers });
  }

  getCustomerDetails(customerID: number) {
    return this.http.get<Customer>(environment.API_URL + Constant.CUSTOMER_METHOD.GET_CUSTOMER(customerID));
  }

  updateCustomer(customer: Customer) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.put<boolean>(environment.API_URL + Constant.CUSTOMER_METHOD.UPDATE_CUSTOMER(customer.customerID), customer, { headers });
  }

}
