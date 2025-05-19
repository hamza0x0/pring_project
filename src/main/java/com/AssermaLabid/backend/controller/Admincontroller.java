package com.AssermaLabid.backend.controller;

import com.AssermaLabid.backend.models.Product;
import com.AssermaLabid.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
    public String admin_ajouter(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        // Dossier où tu veux stocker les images (à adapter)
        String uploadDir = "uploads/";

        // Création du dossier s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom unique pour éviter les collisions
        String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

        // Copier le fichier
        try (InputStream inputStream = imageFile.getInputStream()) {
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Met à jour le chemin dans le produit
        product.setImageUrl("/" + uploadDir + filename);

        // Sauvegarde le produit
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
    public String edit_produit(Model model,@RequestParam("id") int id) {
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
