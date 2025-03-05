package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderController orderController;

    private Product product;
    private Order order;
    private Payment payment; // Add Payment object

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        order = new Order(
                UUID.randomUUID().toString(),
                Collections.singletonList(product),
                System.currentTimeMillis(),
                "John Doe"
        );

        // Initialize a Payment object
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("paymentMethod", "VOUCHER");
        paymentData.put("voucherCode", "VOUCHER123");
        payment = new Payment("VOUCHER", paymentData, order);
    }

    @Test
    void testCreateOrderPage() throws Exception {
        when(productService.findAll()).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Collections.singletonList(product)));

        verify(productService, times(1)).findAll();
    }

    @Test
    void testCreateOrderPost() throws Exception {
        when(productService.getId(anyString())).thenReturn(product);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/order/create")
                        .param("selectedProducts", product.getProductId())
                        .param("author", "John Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/pay/" + order.getId()));

        verify(productService, times(1)).getId(product.getProductId());
        verify(orderService, times(1)).createOrder(any(Order.class));
    }

    @Test
    void testCreateOrderPostNoProductsSelected() throws Exception {
        mockMvc.perform(post("/order/create")
                        .param("author", "John Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/create?error=noProductsSelected"));

        verify(productService, never()).getId(anyString());
        verify(orderService, never()).createOrder(any(Order.class));
    }

    @Test
    void testOrderHistoryForm() throws Exception {
        when(orderService.findAllByAuthor("John Doe")).thenReturn(Collections.singletonList(order));

        mockMvc.perform(get("/order/history")
                        .param("author", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistoryForm"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", Collections.singletonList(order)));

        verify(orderService, times(1)).findAllByAuthor("John Doe");
    }

    @Test
    void testShowOrderHistory() throws Exception {
        when(orderService.findAllByAuthor("John Doe")).thenReturn(Collections.singletonList(order));

        mockMvc.perform(post("/order/history")
                        .param("author", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistory"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", Collections.singletonList(order)));

        verify(orderService, times(1)).findAllByAuthor("John Doe");
    }

    @Test
    void testPayOrderPage() throws Exception {
        when(orderService.findById(order.getId())).thenReturn(order);

        mockMvc.perform(get("/order/pay/" + order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPayment"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("order", order));

        verify(orderService, times(1)).findById(order.getId());
    }

    @Test
    void testPayOrderPageOrderNotFound() throws Exception {
        when(orderService.findById("invalidOrderId")).thenReturn(null);

        mockMvc.perform(get("/order/pay/invalidOrderId"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history?error=orderNotFound"));

        verify(orderService, times(1)).findById("invalidOrderId");
    }

    @Test
    void testProcessPayment() throws Exception {
        when(orderService.findById(order.getId())).thenReturn(order);
        when(paymentService.addPayment(any(Order.class), anyString(), anyMap())).thenReturn(payment);

        mockMvc.perform(post("/order/pay/" + order.getId())
                        .param("paymentMethod", "VOUCHER")
                        .param("voucherCode", "VOUCHER123"))
                .andExpect(status().is3xxRedirection());

        verify(orderService, times(1)).findById(order.getId());
        verify(paymentService, times(1)).addPayment(any(Order.class), eq("VOUCHER"), anyMap());
    }

    @Test
    void testProcessPaymentOrderNotFound() throws Exception {
        when(orderService.findById("invalidOrderId")).thenReturn(null);

        mockMvc.perform(post("/order/pay/invalidOrderId")
                        .param("paymentMethod", "VOUCHER")
                        .param("voucherCode", "VOUCHER123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history?error=orderNotFound"));

        verify(orderService, times(1)).findById("invalidOrderId");
        verify(paymentService, never()).addPayment(any(Order.class), anyString(), anyMap());
    }

    @Test
    void testCancelOrderFailed() throws Exception {
        doThrow(new RuntimeException("Failed to cancel order")).when(orderService).cancelOrder(order.getId());

        mockMvc.perform(post("/order/cancel/" + order.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order/history?error=cancelFailed"));

        verify(orderService, times(1)).cancelOrder(order.getId());
    }
}