package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        sampleProduct = new Product();
        sampleProduct.setProductName("Tumbal Proyek");
        sampleProduct.setProductQuantity(100);
    }

    @Nested
    @DisplayName("Create Product Tests")
    class CreateProductTests {
        @Test
        void testCreateAndFind() {
            Product savedProduct = productRepository.create(sampleProduct);

            assertNotNull(savedProduct.getProductId());

            Iterator<Product> productIterator = productRepository.findAll();
            assertTrue(productIterator.hasNext());

            Product foundProduct = productIterator.next();
            assertEquals(savedProduct.getProductId(), foundProduct.getProductId());
            assertEquals(sampleProduct.getProductName(), foundProduct.getProductName());
            assertEquals(sampleProduct.getProductQuantity(), foundProduct.getProductQuantity());
        }
    }

    @Nested
    class FindProductTests {
        @Test
        void testFindAllIfEmpty() {
            Iterator<Product> productIterator = productRepository.findAll();
            assertFalse(productIterator.hasNext());
        }

        @Test
        void testFindAllIfMoreThanOneProduct() {
            Product firstProduct = new Product();
            firstProduct.setProductName("First Product");
            firstProduct.setProductQuantity(100);
            Product savedFirst = productRepository.create(firstProduct);

            Product secondProduct = new Product();
            secondProduct.setProductName("Second Product");
            secondProduct.setProductQuantity(50);
            Product savedSecond = productRepository.create(secondProduct);

            Iterator<Product> productIterator = productRepository.findAll();

            assertTrue(productIterator.hasNext());
            Product retrievedFirst = productIterator.next();
            assertEquals(savedFirst.getProductId(), retrievedFirst.getProductId());

            assertTrue(productIterator.hasNext());
            Product retrievedSecond = productIterator.next();
            assertEquals(savedSecond.getProductId(), retrievedSecond.getProductId());

            assertFalse(productIterator.hasNext());
        }

        @Test
        void testGetIdWithNullProductId() {
            productRepository.create(sampleProduct);
            assertNull(productRepository.getId(null));
        }
    }

    @Nested
    class UpdateProductTests {
        @Test
        void testEditProductWithPositiveQuantity() {
            Product savedProduct = productRepository.create(sampleProduct);

            Product updatedProduct = new Product();
            updatedProduct.setProductQuantity(250);
            productRepository.update(savedProduct.getProductId(), updatedProduct);

            Product retrievedProduct = productRepository.getId(savedProduct.getProductId());
            assertEquals(250, retrievedProduct.getProductQuantity());
            assertEquals(sampleProduct.getProductName(), retrievedProduct.getProductName());
        }

        @Test
        void testEditProductWithNegativeQuantity() {
            Product savedProduct = productRepository.create(sampleProduct);
            int originalQuantity = savedProduct.getProductQuantity();

            Product updatedProduct = new Product();
            updatedProduct.setProductQuantity(-50);
            productRepository.update(savedProduct.getProductId(), updatedProduct);

            Product retrievedProduct = productRepository.getId(savedProduct.getProductId());
            assertEquals(originalQuantity, retrievedProduct.getProductQuantity());
        }

        @Test
        void testEditProductWithValidName() {
            Product savedProduct = productRepository.create(sampleProduct);
            String newName = "Updated Product Name";

            Product updatedProduct = new Product();
            updatedProduct.setProductName(newName);
            productRepository.update(savedProduct.getProductId(), updatedProduct);

            Product retrievedProduct = productRepository.getId(savedProduct.getProductId());
            assertEquals(newName, retrievedProduct.getProductName());
            assertEquals(sampleProduct.getProductQuantity(), retrievedProduct.getProductQuantity());
        }

        @Test
        void testEditProductWithNullName() {
            Product savedProduct = productRepository.create(sampleProduct);
            String originalName = savedProduct.getProductName();

            Product updatedProduct = new Product();
            updatedProduct.setProductName(null);
            productRepository.update(savedProduct.getProductId(), updatedProduct);

            Product retrievedProduct = productRepository.getId(savedProduct.getProductId());
            assertEquals(originalName, retrievedProduct.getProductName());
        }
    }

    @Nested
    class DeleteProductTests {
        @Test
        void testDeleteProduct() {
            Product savedProduct = productRepository.create(sampleProduct);
            productRepository.delete(savedProduct.getProductId());

            Iterator<Product> productIterator = productRepository.findAll();
            assertFalse(productIterator.hasNext());
        }

        @Test
        void testDeleteProductWithInvalidId() {
            productRepository.create(sampleProduct);
            productRepository.delete("invalid-id");

            Iterator<Product> productIterator = productRepository.findAll();
            assertTrue(productIterator.hasNext());
        }
    }

    @Test
    void testAllFeature() {
        Product savedProduct = productRepository.create(sampleProduct);
        assertNotNull(savedProduct.getProductId());

        String newName = "Updated Product";
        Product updatedProduct = new Product();
        updatedProduct.setProductName(newName);
        updatedProduct.setProductQuantity(-2);
        productRepository.update(savedProduct.getProductId(), updatedProduct);

        Product retrievedProduct = productRepository.getId(savedProduct.getProductId());
        assertEquals(newName, retrievedProduct.getProductName());
        assertEquals(sampleProduct.getProductQuantity(), retrievedProduct.getProductQuantity());

        productRepository.delete(savedProduct.getProductId());
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }
}