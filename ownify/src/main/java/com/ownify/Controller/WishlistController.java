package com.ownify.Controller;

import com.ownify.Entity.User;
import com.ownify.Entity.Product;
import com.ownify.Entity.Wishlist;
import com.ownify.Service.WishlistService;
import com.ownify.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

@Controller
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ProductService productService;

    @GetMapping("/wishlist")
    public String wishlist(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/signin";
        }

        List<Wishlist> wishlistItems = wishlistService.getUserWishlist(user);
        model.addAttribute("wishlistItems", wishlistItems);
        model.addAttribute("user", user);

        return "wishlist";
    }

    @PostMapping("/wishlist/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToWishlist(@RequestParam Long productId, 
                                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please sign in first");
            return ResponseEntity.ok(response);
        }

        try {
            Product product = productService.getProductById(productId).orElse(null);
            if (product == null) {
                response.put("success", false);
                response.put("message", "Product not found");
                return ResponseEntity.ok(response);
            }

            wishlistService.addToWishlist(user, product);
            response.put("success", true);
            response.put("message", "Added to wishlist successfully");

        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/wishlist/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromWishlist(@RequestParam Long productId, 
                                                                HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "Please sign in first");
            return ResponseEntity.ok(response);
        }

        try {
            Product product = productService.getProductById(productId).orElse(null);
            if (product == null) {
                response.put("success", false);
                response.put("message", "Product not found");
                return ResponseEntity.ok(response);
            }

            wishlistService.removeFromWishlist(user, product);
            response.put("success", true);
            response.put("message", "Removed from wishlist successfully");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error removing from wishlist");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/wishlist/check/{productId}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkWishlistStatus(@PathVariable Long productId, 
                                                                  HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("inWishlist", false);
            return ResponseEntity.ok(response);
        }

        Product product = productService.getProductById(productId).orElse(null);
        if (product == null) {
            response.put("inWishlist", false);
            return ResponseEntity.ok(response);
        }

        boolean inWishlist = wishlistService.isInWishlist(user, product);
        response.put("inWishlist", inWishlist);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/wishlist/items")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getWishlistItems(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        List<Wishlist> wishlistItems = wishlistService.getUserWishlist(user);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Wishlist item : wishlistItems) {
            Product product = item.getProduct();
            if (product != null) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getId());
                productMap.put("name", product.getTitle());
                productMap.put("image", product.getImageUrl() != null ? product.getImageUrl() : "https://via.placeholder.com/80x80?text=Product");
                productMap.put("price", product.getPrice());
                productMap.put("category", product.getCategory() != null ? product.getCategory().getName() : "Uncategorized");
                productMap.put("variant", "");
                productMap.put("inStock", true);

                result.add(productMap);
            }
        }

        return ResponseEntity.ok(result);
    }
}
