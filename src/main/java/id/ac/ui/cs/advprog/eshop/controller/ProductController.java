package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product) {
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @GetMapping("/edit")
    public String updateProductPage(@RequestParam String productId, Model model) {
        Product product = service.getId(productId);
        model.addAttribute("product", product);
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product) {
        service.update(product.getProductId(), product);
        return "redirect:/product/list";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam String productId) {
        service.delete(productId);
        return "redirect:list";
    }
}