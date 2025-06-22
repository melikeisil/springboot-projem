package com.ownify.Service;

import com.ownify.Entity.Wishlist;
import com.ownify.Entity.User;
import com.ownify.Entity.Product;
import com.ownify.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
// import java.util.Optional;

@Service
public class WishlistService {
    
    @Autowired
    private WishlistRepository wishlistRepository;
    
    public List<Wishlist> getUserWishlist(User user) {
        return wishlistRepository.findByUser(user);
    }
    
    public Wishlist addToWishlist(User user, Product product) {
        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("Product already in wishlist");
        }
        Wishlist wishlist = new Wishlist(user, product);
        return wishlistRepository.save(wishlist);
    }
    
    @Transactional
    public void removeFromWishlist(User user, Product product) {
        wishlistRepository.deleteByUserAndProduct(user, product);
    }
    
    public boolean isInWishlist(User user, Product product) {
        return wishlistRepository.existsByUserAndProduct(user, product);
    }
}