package com.AssermaLabid.backend.controller;

import org.springframework.ui.Model;


import com.AssermaLabid.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/produit")
public class Productcontroller {
    int page=1;
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public String produit(@PathVariable("id") int id, Model model) {
        model.addAttribute("produit", productService.get_un_product(id));
        return "produit";
    }
    @GetMapping("/list_produit")
    public String list_produit(Model model) {
        model.addAttribute("list_produit",productService.product_page_a());
        page=1;
        return "products_list";

    }
    @PostMapping("/produit_selectioner/{id}")
    public String ProduitSelectioner(@PathVariable("id") int id) {
        
        return"redirect:/list_produit";
    }





}

