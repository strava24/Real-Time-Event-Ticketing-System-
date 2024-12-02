import { Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { LayoutComponent } from './component/layout/layout.component';
import { VendorDashboardComponent } from './component/vendor-dashboard/vendor-dashboard.component';
import { VendorLayoutComponent } from './component/vendor-layout/vendor-layout.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: '',
        component: VendorLayoutComponent,
        children: [
            {
                path: 'vendor-dashboard',
                component: VendorDashboardComponent
            }
        ]
    }
];
