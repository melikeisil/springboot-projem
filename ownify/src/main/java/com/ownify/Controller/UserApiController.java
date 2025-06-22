package com.ownify.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ownify.Entity.User;
import com.ownify.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = "http://localhost:*", allowCredentials = "true")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Kullanıcı kaydı
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userData) {
        try {
            System.out.println("=== RECEIVED USER DATA ===");
            userData.forEach((key, value) -> {
                if (key.equals("password")) {
                    System.out.println(key + ": [HIDDEN]");
                } else {
                    System.out.println(key + ": '" + value + "'");
                }
            });

            // Validation
            Map<String, String> errors = new HashMap<>();

            String firstName = userData.get("firstName");
            String lastName = userData.get("lastName");
            String email = userData.get("email");
            String password = userData.get("password");

            if (firstName == null || firstName.trim().isEmpty()) {
                errors.put("firstName", "First name is required");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                errors.put("lastName", "Last name is required");
            }
            if (email == null || email.trim().isEmpty()) {
                errors.put("email", "Email is required");
            }
            if (password == null || password.trim().isEmpty()) {
                errors.put("password", "Password is required");
            }

            if (!errors.isEmpty()) {
                System.out.println("=== VALIDATION ERRORS ===");
                errors.forEach((field, message) -> System.out.println(field + ": " + message));
                return ResponseEntity.badRequest().body(errors);
            }

            // Create user object
            User user = new User();
            user.setEmail(email.trim());
            user.setPassword(password);
            user.setFirstName(firstName.trim());
            user.setLastName(lastName.trim());

            User registeredUser = userService.registerUser(user);
            System.out.println("=== USER REGISTERED SUCCESSFULLY ===");
            System.out.println("User ID: " + registeredUser.getId());
            System.out.println("Email: " + registeredUser.getEmail());

            return ResponseEntity.ok(registeredUser);

        } catch (Exception e) {
            System.err.println("=== ERROR DURING REGISTRATION ===");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    // Kullanıcı girişi
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, jakarta.servlet.http.HttpSession session) {
        try {
            System.out.println("Login attempt for email: " + credentials.get("email"));
            System.out.println("Session ID before login: " + session.getId());

            Optional<User> user = userService.loginUser(
                credentials.get("email"),
                credentials.get("password")
            );

            if (user.isPresent()) {
                // Set user in session
                session.setAttribute("user", user.get());
                System.out.println("User logged in successfully: " + user.get().getEmail());
                System.out.println("Session ID after login: " + session.getId());
                System.out.println("Session attributes after login: ");
                java.util.Enumeration<String> attributeNames = session.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    String name = attributeNames.nextElement();
                    System.out.println(name + ": " + session.getAttribute(name));
                }
                return ResponseEntity.ok(user.get());
            } else {
                System.out.println("Login failed: Invalid email or password");
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Kullanıcı bilgilerini getir
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Email ile kullanıcı kontrolü
    @GetMapping("/users/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Kullanıcı bilgilerini güncelle
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<User> existingUser = userService.findById(id);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                if (updatedUser.getFirstName() != null) user.setFirstName(updatedUser.getFirstName());
                if (updatedUser.getLastName() != null) user.setLastName(updatedUser.getLastName());
                if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());
                if (updatedUser.getAddress() != null) user.setAddress(updatedUser.getAddress());

                User savedUser = userService.updateUser(user);
                return ResponseEntity.ok(savedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 
