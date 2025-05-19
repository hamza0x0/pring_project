package com.AssermaLabid.backend.services;

import com.AssermaLabid.backend.models.Product;
import com.AssermaLabid.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void ajouter_produit(Product product){
        productRepository.save(product);
    }
    public long somme_produit(){
        return productRepository.count();
    }
    public long en_stock_produit(){
        List<Product> product = productRepository.findAll();
        int s=0;
        for(Product p:product){
            s=s+p.getStock();
        }
        return s;
    }
    public long nbr_produit_null(){
        return productRepository.ChercherProduitNull();
    }
    public long nbr_categorie(){
        return 4;
    }
    public List<Product> get_product(int page){
        List<Product> products = productRepository.findAll();
        List<Product> products1 = new ArrayList<>();
        int startIndex = (page - 1) * 5;
        int endIndex = Math.min(startIndex + 5, products.size());
        for (int i = startIndex; i < endIndex; i++) {
            Product p = products.get(i);

            products1.add(p);
        }

        return products1;
    }
    public Product get_un_product(int id){
        return productRepository.findById(id).orElse(null);
    }
    public void update_un_product(Product product){
         productRepository.save(product);
    }
    public List<Product> product_afficher_f_page(){
       List <Product> p1 = productRepository.findAllParOrder();
       List <Product> p2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            p2.add(p1.get(i));
        }
        return p2;
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
    public List<Product> product_page_a(){
       return productRepository.findAllParOrder();
    }
    public Product getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }



}
