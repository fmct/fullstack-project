package application.controller.impl;

import java.util.List;

import application.controller.IProductController;
import application.utils.ProductNotFoundException;
import application.model.Product;
import org.springframework.web.bind.annotation.*;
import application.service.ProductRepository;

@RestController
@CrossOrigin
public class ProductController implements IProductController {

    private final ProductRepository repository;

    ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/products")
    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/product")
    @Override
    public Product newProduct(@RequestBody Product newProduct) {
        return repository.save(newProduct);
    }

    @GetMapping("/products/{id}")
    @Override
    public Product getSingleProduct(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @PutMapping("/products/{id}")
    @Override
    public Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return repository.findById(id)
                .map(product -> {
                    product.setPrice(newProduct.getPrice());
                    product.setName(newProduct.getName());
                    return repository.save(product);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @DeleteMapping("/products/{id}")
    @Override
    public void deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
