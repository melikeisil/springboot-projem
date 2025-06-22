package com.ownify.Controller;

import com.ownify.Entity.User;
import com.ownify.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "signin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password,
                       HttpSession session, 
                       RedirectAttributes redirectAttributes) {
        System.out.println("Form login attempt for email: " + email);
        System.out.println("Session ID before form login: " + session.getId());

        Optional<User> user = userService.loginUser(email, password);
        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            return "redirect:/dashboard";
        } else {
            System.out.println("Form login failed: Invalid email or password");
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/signin";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, 
                          RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/signin";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "forgotPass";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, 
                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("success", "Password reset link sent to your email");
        return "redirect:/signin";
    }
}
