package com.ownify.Repository;

import com.ownify.Entity.Product;
import com.ownify.Entity.Category;
import com.ownify.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByUser(User user);
    
    @Query("SELECT p FROM Product p WHERE " +
           "p.title LIKE %:keyword% OR " +
           "p.description LIKE %:keyword% OR " +
           "p.brand LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);
    
    List<Product> findByTitleContainingIgnoreCase(String title);
    List<Product> findByBrandContainingIgnoreCase(String brand);
}