package com.ownify.config;

import com.ownify.Entity.Category;
import com.ownify.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        
        if (categoryRepository.count() == 0) {
            
            List<Category> categories = Arrays.asList(
                new Category("Computer & Laptop", "All computer and laptop related products"),
                new Category("Car", "All car related products"),
                new Category("SmartPhone", "All smartphone related products"),
                new Category("TV & Home Appliances", "All TV and home appliances"),
                new Category("Electronic Accessories", "All electronic accessories"),
                new Category("House", "All house related listings"),
                new Category("Land", "All land related listings")
            );
            
           
            categoryRepository.saveAll(categories);
            
            System.out.println("Categories initialized successfully!");
        }
    }
}
