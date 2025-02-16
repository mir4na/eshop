package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product getId(String productId);
    void update(String productId, Product updatedProduct);
    void delete(String productId);
    List<Product> findAll();
}
