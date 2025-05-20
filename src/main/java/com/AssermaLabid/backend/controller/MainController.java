package com.AssermaLabid.backend.controller;

import com.AssermaLabid.backend.models.User;
import com.AssermaLabid.backend.models.role;
import com.AssermaLabid.backend.repository.UserRepository;
import com.AssermaLabid.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.AssermaLabid.backend.config.SecurityConfig.passwordEncoder;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;
    @GetMapping("/login")
    public String login() {
        String hashed = new BCryptPasswordEncoder().encode("1234");
        System.out.println(hashed); // à mettre en base de données

        return "login";  // login.html dans templates
    }
    @GetMapping("/home")
    public String home(Model model) {

        model.addAttribute("product", productService.product_afficher_f_page());
        return "index";
    }
    @GetMapping("")
    public String index(Model model) {

        model.addAttribute("product", productService.product_afficher_f_page());
        return "index";
    }
    @GetMapping("/inscription")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "inscription";  // le nom du fichier HTML dans templates/
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // Tu définis le rôle USER par défaut
        user.setRole(role.USER);
        // Tu encodes le mot de passe
        user.setMotDePasse(passwordEncoder().encode(user.getMotDePasse()));

        // Sauvegarde dans la base
        userRepository.save(user);

        return "redirect:/login";  // ou "/inscription?success"
    }
    @GetMapping("/maintenance")
    public String erreur(Model model) {
        return "erreur";
    }
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        return "/login";
    }


}




