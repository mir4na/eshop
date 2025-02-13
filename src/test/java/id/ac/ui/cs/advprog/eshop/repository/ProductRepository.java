package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testEditProductWithPositiveQuantity() {
        Product product = new Product();
        product.setProductName("Ubur-ubur");
        product.setProductQuantity(150);
        productRepository.create(product);
        String productId = product.getProductId();

        Product updatedProduct = new Product();
        updatedProduct.setProductQuantity(250);
        productRepository.update(productId, updatedProduct);

        assertEquals("Ubur-ubur", product.getProductName());
        assertEquals(250, product.getProductQuantity());
    }

    @Test
    void testEditProductWithNegativeQuantity() {
        Product product = new Product();
        product.setProductName("Lato-lato");
        product.setProductQuantity(150);
        productRepository.create(product);
        String productId = product.getProductId();

        Product updatedProduct = new Product();
        updatedProduct.setProductQuantity(-50);
        productRepository.update(productId, updatedProduct);

        assertEquals("Lato-lato", product.getProductName());
        assertEquals(150, product.getProductQuantity());
    }

    @Test
    void testEditProductWithValidName() {
        Product product = new Product();
        product.setProductName("Lato-lato");
        product.setProductQuantity(150);
        Product savedProduct = productRepository.create(product);
        String productId = savedProduct.getProductId();

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Lato-lato badak");
        productRepository.update(productId, updatedProduct);

        assertEquals("Lato-lato badak", product.getProductName());
        assertEquals(150, product.getProductQuantity());
    }

    @Test
    void testEditProductWithNullName() {
        Product product = new Product();
        product.setProductName("Lato-lato");
        product.setProductQuantity(150);
        productRepository.create(product);
        String productId = product.getProductId();

        Product updatedProduct = new Product();
        updatedProduct.setProductName(null);
        productRepository.update(productId, updatedProduct);

        assertEquals("Lato-lato", product.getProductName());
        assertEquals(150, product.getProductQuantity());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setProductName("Tumbal Proyek");
        product.setProductQuantity(100);
        Product savedProduct = productRepository.create(product);
        String productId = savedProduct.getProductId();

        productRepository.delete(productId);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testDeleteProductWithInvalidId() {
        Product product = new Product();
        product.setProductName("Kecubung Liar");
        product.setProductQuantity(100);
        productRepository.create(product);

        productRepository.delete("invalid-id");

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
    }

    @Test
    void testAllFeature() {
        Product product = new Product();
        product.setProductName("Kecubung Liar");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Kecubung Halal");
        updatedProduct.setProductQuantity(-2);
        productRepository.update(product.getProductId(), updatedProduct);

        assertEquals("Kecubung Halal", product.getProductName());
        assertEquals(100, product.getProductQuantity());

        productRepository.delete(product.getProductId());

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }
}