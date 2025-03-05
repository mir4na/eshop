package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final ProductService productService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(ProductService productService, OrderService orderService, PaymentService paymentService) {
        this.productService = productService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam(value = "selectedProducts", required = false) List<String> selectedProductIds, @RequestParam("author") String author) {
        if (selectedProductIds == null || selectedProductIds.isEmpty()) {
            return "redirect:/order/create?error=noProductsSelected";
        }

        List<Product> selectedProducts = selectedProductIds.stream().map(productService::getId).toList();

        Order order = new Order(
                UUID.randomUUID().toString(),
                selectedProducts,
                System.currentTimeMillis(),
                author
        );

        Order savedOrder = orderService.createOrder(order);

        if (savedOrder == null) {
            return "redirect:/order/create?error=orderCreationFailed";
        }

        return "redirect:/order/pay/" + savedOrder.getId();
    }

    @GetMapping("/history")
    public String orderHistoryForm(
            @RequestParam(value = "author", required = false) String author,
            Model model
    ) {
        if (author != null && !author.isEmpty()) {
            List<Order> orders = orderService.findAllByAuthor(author);
            model.addAttribute("orders", orders);
        }
        return "OrderHistoryForm";
    }

    @PostMapping("/history")
    public String showOrderHistory(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "OrderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "redirect:/order/history?error=orderNotFound";
        }
        model.addAttribute("order", order);
        return "OrderPayment";
    }

    @PostMapping("/pay/{orderId}")
    public String processPayment(
            @PathVariable String orderId,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam(value = "voucherCode", required = false) String voucherCode,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "deliveryFee", required = false) Double deliveryFee
    ) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "redirect:/order/history?error=orderNotFound";
        }

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("paymentMethod", paymentMethod);

        if (paymentMethod.equals("VOUCHER")) {
            paymentData.put("voucherCode", voucherCode);
        } else if (paymentMethod.equals("COD")) {
            paymentData.put("address", address);
            paymentData.put("deliveryFee", String.valueOf(deliveryFee));
        }

        var payment = paymentService.addPayment(order, paymentMethod, paymentData);

        if (payment.getStatus().equals("WAITING_PAYMENT")) {
            return "redirect:/order/history?success=paymentProcessed";
        } else {
            return "redirect:/order/pay/" + orderId + "?error=paymentFailed";
        }
    }

    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@PathVariable String orderId) {
        try {
            orderService.cancelOrder(orderId);
            return "redirect:/order/history?success=orderCancelled";
        } catch (Exception e) {
            return "redirect:/order/history?error=cancelFailed";
        }
    }
}