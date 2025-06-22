package com.ownify.Controller;

import com.ownify.Entity.Product;
import com.ownify.Entity.Category;
import com.ownify.Service.ProductService;
import com.ownify.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String q, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/signin";
        }
        
        List<Product> searchResults = productService.searchProducts(q);
        List<Category> categories = categoryService.getAllCategories();
        
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("categories", categories);
        model.addAttribute("searchQuery", q);
        return "shopPage";
    }
    
    @GetMapping("/shop")
    public String shop(Model model, HttpSession session) {
        List<Category> categories = categoryService.getAllCategories();
        List<Product> products = productService.getAllProducts();
        
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "shopPage";
    }
    
    @GetMapping("/category/{id}")
    public String categoryProducts(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/signin";
        }
        
        List<Product> products = productService.getProductsByCategoryId(id);
        Category category = categoryService.getCategoryById(id).orElse(null);
        List<Category> categories = categoryService.getAllCategories();
        
        model.addAttribute("products", products);
        model.addAttribute("currentCategory", category);
        model.addAttribute("categories", categories);
        return "shopPage";
    }
    
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/signin";
        }
        
        Product product = productService.getProductById(id).orElse(null);
        if (product == null) {
            return "redirect:/";
        }
        
        model.addAttribute("product", product);
        return "productDetail";
    }
    
    @GetMapping("/about")
    public String about() {
        return "aboutUs";
    }
    
    @GetMapping("/faqs")
    public String faqs() {
        return "faqs";
    }
    
    @GetMapping("/customer-support")
    public String customerSupport() {
        return "customerSupport";
    }
}