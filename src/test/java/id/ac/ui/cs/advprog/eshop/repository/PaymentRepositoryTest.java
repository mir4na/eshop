package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment payment1;
    private Payment payment2;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        order = createSampleOrder();
        payment1 = createPayment(PaymentMethod.VOUCHER, Map.of("voucherCode", "ESHOP1234ABC5678"));
        payment2 = createPayment(PaymentMethod.COD, Map.of("address", "Jl. Walet Indah 3, No. 10", "deliveryFee", "12000"));
    }

    private Order createSampleOrder() {
        Product product = new Product();
        product.setProductName("Electronics");
        product.setProductQuantity(2);
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        return new Order("13652556-012a-4c07-b546-54eb1396d79b", List.of(product), 1708560000L, "Safira Sudrajat");
    }

    private Payment createPayment(PaymentMethod method, Map<String, String> paymentData) {
        return new Payment(method.getValue(), paymentData, order);
    }

    @Test
    void testSaveNewPayment() {
        Payment savedPayment = paymentRepository.save(payment1);
        assertNotNull(savedPayment.getId());
        assertPaymentFound(savedPayment);
    }

    @Test
    void testSaveExistingPayment() {
        Payment savedPayment = paymentRepository.save(payment1);
        savedPayment.setStatus("REJECTED");
        Payment updatedPayment = paymentRepository.save(savedPayment);

        assertEquals("REJECTED", updatedPayment.getStatus());
        assertEquals(1, paymentRepository.findAll().size());
    }

    @Test
    void testFindById() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        assertPaymentFound(payment2);
    }

    @Test
    void testFindByIdNotFound() {
        assertNull(paymentRepository.findById("non-existent-id"));
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(2, allPayments.size());
        assertTrue(allPayments.containsAll(List.of(payment1, payment2)));
    }

    @Test
    void testFindAllEmptyRepository() {
        assertTrue(paymentRepository.findAll().isEmpty());
    }

    private void assertPaymentFound(Payment payment) {
        Payment foundPayment = paymentRepository.findById(payment.getId());
        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
    }
}