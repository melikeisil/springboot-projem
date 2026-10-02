package com.ownify.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ownify.Entity.User;
import com.ownify.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

   
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userData) {
        try {
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
                return ResponseEntity.badRequest().body(errors);
            }

            
            User user = new User();
            user.setEmail(email.trim());
            user.setPassword(password);
            user.setFirstName(firstName.trim());
            user.setLastName(lastName.trim());

            User registeredUser = userService.registerUser(user);

            return ResponseEntity.ok(registeredUser);

        } catch (Exception e) {
            System.err.println("=== ERROR DURING REGISTRATION ===");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

  
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, HttpSession session) {
        try {
            Optional<User> user = userService.loginUser(
                credentials.get("email"),
                credentials.get("password")
            );

            if (user.isPresent()) {
                session.setAttribute("user", user.get());
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, HttpSession session) {
        ResponseEntity<?> denied = checkOwnership(id, session);
        if (denied != null) {
            return denied;
        }

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping("/users/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser, BindingResult bindingResult, HttpSession session) {
        ResponseEntity<?> denied = checkOwnership(id, session);
        if (denied != null) {
            return denied;
        }

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
                session.setAttribute("user", savedUser);
                return ResponseEntity.ok(savedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Returns 401 if no user is logged in, 403 if the logged-in user is not the owner of the record, otherwise null.
    private ResponseEntity<?> checkOwnership(Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in"));
        }
        if (!id.equals(sessionUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Access denied"));
        }
        return null;
    }
}
