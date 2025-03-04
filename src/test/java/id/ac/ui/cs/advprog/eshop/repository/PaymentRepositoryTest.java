package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment payment1;
    private Payment payment2;
    Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);
        order = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");

        Map<String, String> voucherPaymentData = new HashMap<>();
        voucherPaymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment1 = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, order);

        Map<String, String> codPaymentData = new HashMap<>();
        codPaymentData.put("address", "Jl. Walet Indah 3, No. 10");
        codPaymentData.put("deliveryFee", "12000");
        payment2 = new Payment(PaymentMethod.COD.getValue(), codPaymentData, order);
    }

    @Test
    void testSaveNewPayment() {
        Payment savedPayment = paymentRepository.save(payment1);
        assertNotNull(savedPayment.getId());

        Payment foundPayment = paymentRepository.findById(savedPayment.getId());
        assertNotNull(foundPayment);
        assertEquals(savedPayment.getId(), foundPayment.getId());
    }

    @Test
    void testSaveExistingPayment() {
        Payment savedPayment = paymentRepository.save(payment1);

        savedPayment.setStatus("REJECTED");
        Payment updatedPayment = paymentRepository.save(savedPayment);

        assertEquals("REJECTED", updatedPayment.getStatus());

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(1, allPayments.size());
    }

    @Test
    void testFindById() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        Payment foundPayment = paymentRepository.findById(payment2.getId());
        assertNotNull(foundPayment);
        assertEquals(payment2.getId(), foundPayment.getId());
        assertEquals(PaymentMethod.COD.getValue(), foundPayment.getMethod());
    }

    @Test
    void testFindByIdNotFound() {
        Payment foundPayment = paymentRepository.findById("non-existent-id");
        assertNull(foundPayment);
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
        assertTrue(allPayments.contains(payment1));
        assertTrue(allPayments.contains(payment2));
    }

    @Test
    void testFindAllEmptyRepository() {
        List<Payment> allPayments = paymentRepository.findAll();
        assertTrue(allPayments.isEmpty());
    }
}