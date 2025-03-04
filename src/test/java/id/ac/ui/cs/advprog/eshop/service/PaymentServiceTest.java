package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import enums.PaymentMethod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order mockOrder;
    private Map<String, String> voucherPaymentData;
    private Map<String, String> codPaymentData;

    @BeforeEach
    void setUp() {
        mockOrder = createMockOrder();
        voucherPaymentData = createVoucherPaymentData();
        codPaymentData = createCODPaymentData();
    }

    private Order createMockOrder() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);
        products.add(product);
        return new Order("order-1", products, 1708560000L, "Test User");
    }

    private Map<String, String> createVoucherPaymentData() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        return data;
    }

    private Map<String, String> createCODPaymentData() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Test Address");
        data.put("deliveryFee", "10000");
        return data;
    }

    @Test
    void testAddVoucherPayment() {
        Payment payment = performPaymentTest(PaymentMethod.VOUCHER.getValue(), voucherPaymentData);
        validatePaymentResult(payment, PaymentMethod.VOUCHER.getValue());
    }

    @Test
    void testAddCODPayment() {
        Payment payment = performPaymentTest(PaymentMethod.COD.getValue(), codPaymentData);
        validatePaymentResult(payment, PaymentMethod.COD.getValue());
    }

    private Payment performPaymentTest(String paymentMethod, Map<String, String> paymentData) {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        return paymentService.addPayment(mockOrder, paymentMethod, paymentData);
    }

    private void validatePaymentResult(Payment payment, String expectedMethod) {
        assertNotNull(payment);
        assertEquals(expectedMethod, payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(mockOrder, payment.getOrder());
        assertEquals("SUCCESS", mockOrder.getStatus());

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(mockOrder);
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, mockOrder);
        payment.setStatus("REJECTED");
        mockOrder.setStatus("FAILED");

        Payment updatedPayment = performStatusUpdateTest(payment, "SUCCESS");

        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals("SUCCESS", mockOrder.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, mockOrder);
        mockOrder.setStatus("SUCCESS");

        Payment updatedPayment = performStatusUpdateTest(payment, "REJECTED");

        assertEquals("REJECTED", updatedPayment.getStatus());
        assertEquals("FAILED", mockOrder.getStatus());
    }

    private Payment performStatusUpdateTest(Payment payment, String status) {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment updatedPayment = paymentService.setStatus(payment, status);

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(mockOrder);

        return updatedPayment;
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, mockOrder);
        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment foundPayment = paymentService.getPayment(payment.getId());

        assertNotNull(foundPayment);
        assertSame(payment, foundPayment);
        verify(paymentRepository).findById(payment.getId());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = createTestPayments();
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> allPayments = paymentService.getAllPayments();

        assertEquals(2, allPayments.size());
        assertTrue(allPayments.contains(payments.get(0)));
        assertTrue(allPayments.contains(payments.get(1)));
        verify(paymentRepository).findAll();
    }

    private List<Payment> createTestPayments() {
        List<Payment> payments = new ArrayList<>();
        Payment payment1 = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, mockOrder);
        Payment payment2 = new Payment(PaymentMethod.COD.getValue(), codPaymentData, mockOrder);
        payments.add(payment1);
        payments.add(payment2);
        return payments;
    }
}