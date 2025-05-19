package com.AssermaLabid.backend.controller;

import com.AssermaLabid.backend.models.Product;
import com.AssermaLabid.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class Admincontroller {
    int page=1;
    @Autowired
    private ProductService productService;
    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("nbr_Produits",productService.somme_produit());
        model.addAttribute("produits_en_stock",productService.en_stock_produit());
        model.addAttribute("ProduitNull",productService.nbr_produit_null());
        model.addAttribute("nbr_Categories",productService.nbr_categorie());
        model.addAttribute("produit",productService.get_product(page));
        page=1;
        return "admin_ajouter";
    }
    @PostMapping("")
    public String admin_ajouter(@ModelAttribute Product product ) {
        productService.ajouter_produit(product);
        return "redirect:/admin";
    }
    @PostMapping("/deleteproduit")
    public String delete_produit(@RequestParam("id") int id) {
        System.out.println(page);
        productService.deleteProductById(id);
        return "redirect:/admin";
    }
    @PostMapping("/editProduit")
    public String edit_produit(@RequestParam("id") int id, Model model) {
        System.out.println(id);
        Product p = productService.get_un_product(id);
        model.addAttribute("produit", p);
        return "edit";
    }
    @PostMapping("/editProduitConfirmation")
    public String edit_product(@ModelAttribute Product product) {
        productService.update_un_product(product);
        return "redirect:/admin";
    }
    @GetMapping("/nextP/{id}")
    public String next_page(@PathVariable("id") int id) {
        if(id>0){
            page = id;

        }
        else{
            if (id==-2 && page>1){
                page--;

            }
            if (id==-1){
                page++;

            }

        }
        System.out.println(page);

        System.out.println(id);
        return "redirect:/admin";
    }
}
