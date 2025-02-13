package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        product.setProductId(UUID.randomUUID().toString());
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product getId(String productId) {
        if (productId == null) return null;
        for (Product product : productData) {
            if (productId.equals(product.getProductId())) {
                return product;
            }
        }
        return null;
    }

    public void update(String productId, Product updatedProduct) {
        Product product = getId(productId);
        if (updatedProduct.getProductName() != null) {
            product.setProductName(updatedProduct.getProductName());
        }

        if (updatedProduct.getProductQuantity() > 0) {
            product.setProductQuantity(updatedProduct.getProductQuantity());
        }
    }
}