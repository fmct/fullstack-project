import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProductsComponent } from './products/products.component';
import { ProductStartComponent } from './products/product-start/product-start.component';
import { ProductDetailComponent } from './products/product-detail/product-detail.component';
import { ProductEditComponent } from './products/product-edit/product-edit.component';
import { ProductsResolverService } from './products/products-resolver.service';
import { AuthComponent } from './auth/auth.component';
import { AuthGuard } from './auth/auth.guard';
import { ProductsGuard } from './products/products.guard';

const appRoutes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  {
    path: 'products',
    component: ProductsComponent,
    canActivate: [ProductsGuard],
    resolve: [ProductsResolverService],
    children: [
      { path: '', component: ProductStartComponent },
      { path: 'new', component: ProductEditComponent },
      {
        path: ':id',
        component: ProductDetailComponent,
        resolve: [ProductsResolverService]
      },
      {
        path: ':id/edit',
        component: ProductEditComponent,
        resolve: [ProductsResolverService]
      }
    ]
  },
  { path: 'auth', component: AuthComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/products' },
];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
