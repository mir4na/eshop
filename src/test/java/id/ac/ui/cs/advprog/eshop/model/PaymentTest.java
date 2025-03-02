package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Product 1");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-123", products, System.currentTimeMillis(), "user1", "WAITING_PAYMENT");

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = Payment.builder()
                .id("payment-123")
                .method("VOUCHER")
                .status("SUCCESS")
                .paymentData(paymentData)
                .order(order)
                .build();
    }

    @Test
    void testPaymentCreation() {
        assertNotNull(payment);
        assertEquals("payment-123", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }

    @Test
    void testSetStatus() {
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetMethod() {
        payment.setMethod("CASH_ON_DELIVERY");
        assertEquals("CASH_ON_DELIVERY", payment.getMethod());
    }
}