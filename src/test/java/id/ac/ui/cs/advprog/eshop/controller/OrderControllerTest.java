package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private OrderController orderController;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderController = new OrderController(productService, orderService, paymentService);
    }

    @Test
    void testCreateOrderPage() {
        List<Product> mockProducts = new ArrayList<>();
        when(productService.findAll()).thenReturn(mockProducts);

        String viewName = orderController.createOrderPage(model);

        assertEquals("CreateOrder", viewName);
        verify(model).addAttribute("products", mockProducts);
    }

    @Test
    void testCreateOrderWithoutProducts() {
        String result = orderController.createOrder(null, "testUser");

        assertEquals("redirect:/order/create?error=noProductsSelected", result);
    }

    @Test
    void testCreateOrderWithEmptyProductList() {
        List<String> emptyProductIds = new ArrayList<>();

        String result = orderController.createOrder(emptyProductIds, "testUser");

        assertEquals("redirect:/order/create?error=noProductsSelected", result);
    }

    @Test
    void testCreateOrderSuccessful() {
        List<String> productIds = List.of("product1", "product2");
        Product mockProduct1 = new Product();
        Product mockProduct2 = new Product();

        when(productService.getId("product1")).thenReturn(mockProduct1);
        when(productService.getId("product2")).thenReturn(mockProduct2);

        Order mockOrder = mock(Order.class);
        when(orderService.createOrder(any(Order.class))).thenReturn(mockOrder);
        when(mockOrder.getId()).thenReturn("testOrderId");

        String result = orderController.createOrder(productIds, "testUser");

        assertEquals("redirect:/order/pay/testOrderId", result);
    }

    @Test
    void testCreateOrderFailure() {
        List<String> productIds = List.of("product1", "product2");
        Product mockProduct1 = new Product();
        Product mockProduct2 = new Product();

        when(productService.getId("product1")).thenReturn(mockProduct1);
        when(productService.getId("product2")).thenReturn(mockProduct2);

        when(orderService.createOrder(any(Order.class))).thenReturn(null);

        String result = orderController.createOrder(productIds, "testUser");

        assertEquals("redirect:/order/create?error=orderCreationFailed", result);
    }

    @Test
    void testOrderHistoryForm() {
        String viewName = orderController.orderHistoryForm();
        assertEquals("OrderHistoryForm", viewName);
    }

    @Test
    void testShowOrderHistory() {
        List<Order> mockOrders = new ArrayList<>();
        when(orderService.findAllByAuthor("testUser")).thenReturn(mockOrders);

        String viewName = orderController.showOrderHistory("testUser", model);

        assertEquals("OrderHistory", viewName);
        verify(model).addAttribute("orders", mockOrders);
    }

    @Test
    void testPayOrderPageSuccess() {
        Order mockOrder = mock(Order.class);
        when(orderService.findById("testOrderId")).thenReturn(mockOrder);

        String viewName = orderController.payOrderPage("testOrderId", model);

        assertEquals("OrderPayment", viewName);
        verify(model).addAttribute("order", mockOrder);
    }

    @Test
    void testPayOrderPageNotFound() {
        when(orderService.findById("testOrderId")).thenReturn(null);

        String viewName = orderController.payOrderPage("testOrderId", model);

        assertEquals("redirect:/order/history?error=orderNotFound", viewName);
    }

    @Test
    void testProcessPaymentSuccess() {
        Order mockOrder = mock(Order.class);
        when(orderService.findById("testOrderId")).thenReturn(mockOrder);

        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getId()).thenReturn("testPaymentId");
        when(paymentService.addPayment(eq(mockOrder), anyString(), any())).thenReturn(mockPayment);

        String viewName = orderController.processPayment("testOrderId", "VOUCHER", model);

        assertEquals("SuccessPayment", viewName);
        verify(model).addAttribute("paymentId", "testPaymentId");
    }

    @Test
    void testProcessPaymentNotFound() {
        when(orderService.findById("testOrderId")).thenReturn(null);

        String viewName = orderController.processPayment("testOrderId", "VOUCHER", model);

        assertEquals("redirect:/order/history?error=orderNotFound", viewName);
    }
}