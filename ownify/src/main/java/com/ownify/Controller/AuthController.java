package com.ownify.Controller;

import com.ownify.Entity.User;
import com.ownify.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
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
                       HttpServletRequest request,
                       HttpSession session, 
                       RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.loginUser(email, password);
        if (user.isPresent()) {
            // Issue a new session id on login to prevent session fixation
            request.changeSessionId();
            session.setAttribute("user", user.get());
            return "redirect:/dashboard";
        } else {
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
            // Ignore any client-supplied id so registration can never overwrite an existing user
            user.setId(null);
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
