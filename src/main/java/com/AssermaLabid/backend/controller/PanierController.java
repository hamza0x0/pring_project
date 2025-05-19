package com.AssermaLabid.backend.controller;

import com.AssermaLabid.backend.models.User;
import com.AssermaLabid.backend.services.CartService;
import com.AssermaLabid.backend.services.UserUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/panier")
public class PanierController {
    @Autowired
    private CartService cartService;

    @PostMapping("/ajouter_article")
    public String panier(@RequestParam("id") int id){
        User user = UserUtil.getCurrentUser();
        cartService.ajouter_panier(id, user);
        return "redirect:/produit/list_produit";
    }

    @GetMapping("")
    public String panier(Model model, HttpServletResponse response){
        User user = UserUtil.getCurrentUser();
        long id = user.getCart().getId();
        model.addAttribute("list_produit", cartService.voir_panier(id));
        model.addAttribute("total", cartService.total_panier(id)+50+(cartService.total_panier(id)*0.2));

        return "panier";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        System.out.println(id);
        cartService.delete_panier(id);


        return "redirect:/panier";
    }
    @GetMapping("/achat_fini")
    public String achat_fini(Model model) {
        User user = UserUtil.getCurrentUser();
        cartService.acheter_panier(user);
        return "redirect:/home";
    }
}
