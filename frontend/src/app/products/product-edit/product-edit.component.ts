import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ProductService } from '../product.service';
import { DataStorageService } from 'src/app/shared/data-storage.service';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {
  id: number;
  editMode = false;
  productForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private dataStorageService: DataStorageService,
    private productService: ProductService,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.id = +params['id'];
      this.editMode = params['id'] != null;
      this.initForm();
    });
  }

  onSubmit() {
   
    if (this.editMode) {
      this.dataStorageService.updateProduct(this.id, this.productForm.value).subscribe();
    } else {
      this.dataStorageService.storeProduct(this.productForm.value).subscribe();
    }
    this.onCancel();
  }

  onCancel() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  private initForm() {
    let productName = '';
    let productPrice = 0;

    if (this.editMode) {
      const product = this.productService.getProduct(this.id);
      productName = product.name;
      productPrice = product.price;      
    }

    this.productForm = new FormGroup({
      name: new FormControl(productName, Validators.required),
      price: new FormControl(productPrice, Validators.required),
    });
  }
}
