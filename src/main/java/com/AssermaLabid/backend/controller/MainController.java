package com.AssermaLabid.backend.controller;

import com.AssermaLabid.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
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
}




