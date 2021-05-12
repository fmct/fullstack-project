package application.controller;

import application.model.Product;

import java.util.List;

public interface IProductController {

    List<Product> getAllProducts();

    Product newProduct(Product newProduct);

    Product getSingleProduct(Long id);

    Product updateProduct(Product newProduct, Long id);

    void deleteProduct(Long id);
}
