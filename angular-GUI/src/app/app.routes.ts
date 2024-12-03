import { Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { LayoutComponent } from './component/layout/layout.component';
import { VendorDashboardComponent } from './component/vendor-dashboard/vendor-dashboard.component';
import { VendorLayoutComponent } from './component/vendor-layout/vendor-layout.component';
import { vendorGuard } from './guard/vendor.guard';
import { SignupComponent } from './component/signup/signup.component';
import { VendorProfileComponent } from './component/vendor-profile/vendor-profile.component';

export const routes: Routes = [
    {
        path: 'login',
        component: LoginComponent
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
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    }
];
