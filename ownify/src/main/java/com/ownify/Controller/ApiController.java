package com.ownify.Controller;

import com.ownify.Entity.Category;
import com.ownify.Entity.Product;
import com.ownify.Entity.User;
import com.ownify.Service.CategoryService;
import com.ownify.Service.ProductService;
import com.ownify.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{id}/products")
    public ResponseEntity<List<Product>> getCategoryProducts(@PathVariable Long id) {
        List<Product> products = productService.getProductsByCategoryId(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam String q) {
        Map<String, Object> response = new HashMap<>();

        if (q == null || q.trim().isEmpty()) {
            response.put("products", List.of());
            response.put("count", 0);
            return ResponseEntity.ok(response);
        }

        List<Product> products = productService.searchProducts(q.trim());
        response.put("products", products);
        response.put("count", products.size());
        response.put("query", q);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/featured")
    public ResponseEntity<List<Product>> getFeaturedProducts(@RequestParam(defaultValue = "8") int limit) {
        List<Product> allProducts = productService.getAllProducts();

        List<Product> featuredProducts = allProducts.stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(limit)
                .toList();

        return ResponseEntity.ok(featuredProducts);
    }

    @GetMapping("/products/random")
    public ResponseEntity<List<Product>> getRandomProducts(@RequestParam(defaultValue = "6") int limit) {
        List<Product> allProducts = productService.getAllProducts();

       
        java.util.Collections.shuffle(allProducts);
        List<Product> randomProducts = allProducts.stream()
                .limit(limit)
                .toList();

        return ResponseEntity.ok(randomProducts);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody Map<String, Object> productData, HttpSession session) {
        try {
           
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(401).body(Map.of("error", "User not logged in"));
            }

           
            String title = (String) productData.get("title");
            String description = (String) productData.get("description");
            BigDecimal price = new BigDecimal(productData.get("price").toString());
            Long categoryId = Long.valueOf(productData.get("categoryId").toString());
            String imageUrl = (String) productData.get("imageUrl");

            // Validate required fields
            if (title == null || title.trim().isEmpty() || price == null || categoryId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
            }

            
            Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

           
            Product product = new Product();
            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(price);
            product.setCategory(category);
            product.setUser(user);
            product.setImageUrl(imageUrl);

          
            if (productData.get("brand") != null) {
                product.setBrand((String) productData.get("brand"));
            }

            if (productData.get("location") != null) {
                product.setLocation((String) productData.get("location"));
            }

            Product savedProduct = productService.saveProduct(product);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Product created successfully",
                "product", savedProduct
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        try {
            return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
}
