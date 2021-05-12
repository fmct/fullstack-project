import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { DataStorageService } from 'src/app/shared/data-storage.service';

import { Product } from '../product.model';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  product: Product;
  id: number;
  subscription: Subscription;


  constructor(private productService: ProductService,
              private dataStorageService: DataStorageService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          this.id = +params['id'];
          this.product = this.productService.getProduct(this.id);
        }
      );

    this.subscription = this.productService.productsChanged
      .subscribe(
        (products: Product[]) => {
          this.product = products[this.id];
        }
      );
  }

  onEditProduct() {
    this.router.navigate(['edit'], {relativeTo: this.route});
  }

  onDeleteProduct() {
    this.dataStorageService.deleteProduct(this.id).subscribe();
    this.router.navigate(['/products']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
