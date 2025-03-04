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
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Order order;
    private Map<String, String> voucherPaymentData;
    private Map<String, String> codPaymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);
        products.add(product);
        order = new Order("order-1", products, 1708560000L, "Test User");

        voucherPaymentData = new HashMap<>();
        voucherPaymentData.put("voucherCode", "ESHOP1234ABC5678");

        codPaymentData = new HashMap<>();
        codPaymentData.put("address", "Test Address");
        codPaymentData.put("deliveryFee", "10000");
    }

    @Test
    void testAddVoucherPayment() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), voucherPaymentData);

        assertNotNull(payment);
        assertEquals(PaymentMethod.VOUCHER.getValue(), payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(order, payment.getOrder());
        assertEquals("SUCCESS", order.getStatus());

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(order);
    }

    @Test
    void testAddCODPayment() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, PaymentMethod.COD.getValue(), codPaymentData);

        assertNotNull(payment);
        assertEquals(PaymentMethod.COD.getValue(), payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertSame(order, payment.getOrder());
        assertEquals("SUCCESS", order.getStatus());

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(order);
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, order);
        payment.setStatus("REJECTED");
        order.setStatus("FAILED");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment updatedPayment = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals("SUCCESS", order.getStatus());

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(order);
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, order);
        order.setStatus("SUCCESS");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment updatedPayment = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", updatedPayment.getStatus());
        assertEquals("FAILED", order.getStatus());

        verify(paymentRepository).save(payment);
        verify(orderRepository).save(order);
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, order);
        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment foundPayment = paymentService.getPayment(payment.getId());

        assertNotNull(foundPayment);
        assertSame(payment, foundPayment);

        verify(paymentRepository).findById(payment.getId());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        Payment payment1 = new Payment(PaymentMethod.VOUCHER.getValue(), voucherPaymentData, order);
        Payment payment2 = new Payment(PaymentMethod.COD.getValue(), codPaymentData, order);
        payments.add(payment1);
        payments.add(payment2);

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> allPayments = paymentService.getAllPayments();

        assertEquals(2, allPayments.size());
        assertTrue(allPayments.contains(payment1));
        assertTrue(allPayments.contains(payment2));

        verify(paymentRepository).findAll();
    }
}