package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;

public interface ProductService {
    public Product create(Product product);
    public Product getId(String productId);
    public void update(String productId, Product updatedProduct);
    public void delete(String productId);
    public List<Product> findAll();
}
