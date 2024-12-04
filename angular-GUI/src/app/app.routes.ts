import { Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { VendorDashboardComponent } from './component/vendor-dashboard/vendor-dashboard.component';
import { VendorLayoutComponent } from './component/vendor-layout/vendor-layout.component';
import { vendorGuard } from './guard/vendor.guard';
import { SignupComponent } from './component/signup/signup.component';
import { VendorProfileComponent } from './component/vendor-profile/vendor-profile.component';
import { CustomerProfileComponent } from './component/customer-profile/customer-profile.component';
import { EventsPageComponent } from './component/events-page/events-page.component';
import { CustomerLayoutComponent } from './component/customer-layout/customer-layout.component';
import { customerGuard } from './guard/customer.guard';

export const routes: Routes = [
    {
        path: 'login',
        component: LoginComponent,
    },
    {
        path: 'signup',
        component: SignupComponent
    },
    {
        path: 'vendor',
        component: VendorLayoutComponent,
        canActivate: [vendorGuard],
        children: [
            {
                path: 'vendor-dashboard',
                component: VendorDashboardComponent
            },
            {
                path: 'vendor-profile',
                component: VendorProfileComponent
            }
        ]
    },
    {
        path: 'customer',
        component: CustomerLayoutComponent,
        canActivate: [customerGuard],
        children: [
            {
                path: 'events',
                component: EventsPageComponent
            },
            {
                path: 'customer-profile',
                component: CustomerProfileComponent
            }
        ]
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'prefix'
    }
];
