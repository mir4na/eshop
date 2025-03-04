package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    public void setup() {
        testProduct = new Product();
        testProduct.setProductName("Test Product");
        testProduct.setProductQuantity(10);
        testProduct = productService.create(testProduct);

        List<Product> products = new ArrayList<>();
        products.add(testProduct);
        testOrder = new Order(
                "test-order-id",
                products,
                System.currentTimeMillis(),
                "testUser"
        );
        testOrder = orderService.createOrder(testOrder);
    }

    @Test
    public void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/create-order"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    public void testCreateOrderSubmission() throws Exception {
        mockMvc.perform(post("/order/create")
                        .param("author", "testUser")
                        .param("selectedProducts", testProduct.getProductId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/order/pay/*"));
    }

    @Test
    public void testCreateOrderWithNoProducts() throws Exception {
        mockMvc.perform(post("/order/create")
                        .param("author", "testUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/create?error=noProductsSelected"));
    }

    @Test
    public void testOrderHistoryForm() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history-form"));
    }

    @Test
    public void testOrderHistorySubmission() throws Exception {
        mockMvc.perform(post("/order/history")
                        .param("author", "testUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/history"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", not(empty())));
    }

    @Test
    public void testOrderPaymentPage() throws Exception {
        mockMvc.perform(get("/order/pay/" + testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order/pay"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    public void testOrderPaymentSubmission() throws Exception {
        mockMvc.perform(post("/order/pay/" + testOrder.getId())
                        .param("paymentMethod", "VOUCHER"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/payment-success"))
                .andExpect(model().attributeExists("paymentId"));
    }

    @Test
    public void testOrderPaymentWithInvalidOrder() throws Exception {
        mockMvc.perform(get("/order/pay/non-existent-order"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history?error=orderNotFound"));
    }
}