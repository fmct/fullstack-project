import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';

import { Product } from '../products/product.model';
import { ProductService } from '../products/product.service';
import { AuthService } from '../auth/auth.service';
import { throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class DataStorageService {
  constructor(private http: HttpClient, private authService: AuthService, private productService: ProductService) {}

  storeProduct(newProduct: Product) {
    return this.http
      .post(
        `${environment.backendUri}/product`,
        newProduct
      ).pipe(
        tap((product: Product) => {
          this.productService.addProduct(product);
        })
      )
  }

  fetchProducts() {
    return this.http
      .get<Product[]>(
        `${environment.backendUri}/products`
      )
      .pipe(
        catchError(this.handleError.bind(this)),
        tap(products => {
          this.productService.setProducts(products);
        })
      )
  }

  fetchProduct(index: number) {
    return this.http
      .get<Product>(
        `${environment.backendUri}/products/${index}`
      )
      .pipe(
        tap(product => {
          this.productService.addProduct(product);
        })
      )
  }

  updateProduct(index: number, newProduct: Product) {
    const id  = this.productService.getProductId(index);
    return this.http
      .put(
        `${environment.backendUri}/products/${id}`,
        newProduct
      )
      .pipe(
        tap((returnedProduct: Product) => {
          this.productService.updateProduct(index, returnedProduct);
        })
      )
  }

  deleteProduct(index: number) {
    const id  = this.productService.getProductId(index);
    return this.http
      .delete(
        `${environment.backendUri}/products/${id}`,
      )
      .pipe(
        tap(() => {
          this.productService.deleteProduct(index);
        })
      )
  }

  private handleError(errorResp: HttpErrorResponse) {
      this.authService.logout();
      return throwError(errorResp);
  }
  
}
