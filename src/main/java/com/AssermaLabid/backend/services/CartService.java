package com.AssermaLabid.backend.services;

import com.AssermaLabid.backend.models.Cart;
import com.AssermaLabid.backend.models.CartItem;
import com.AssermaLabid.backend.models.Product;
import com.AssermaLabid.backend.models.User;
import com.AssermaLabid.backend.repository.CartItemRepository;
import com.AssermaLabid.backend.repository.CartRepository;
import com.AssermaLabid.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartitemrepository;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;


    @Transactional
    public void ajouter_panier(int produitId, User user) {
        Product product = productService.getProductById(produitId);
        if (product == null) {
            throw new IllegalArgumentException("Produit introuvable avec l'ID : " + produitId);
        }

        if (product.getStock() <= 0) {
            throw new IllegalStateException("Produit en rupture de stock.");
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            user.setCart(cart);
        }

        // Initialiser les items si besoin
        Hibernate.initialize(cart.getItems());

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantite(item.getQuantite() + 1);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantite(1);
            cart.getItems().add(newItem);
        }

        // Diminuer le stock du produit
        product.setStock(product.getStock() - 1);

        // Sauvegarder les entités
        productService.ajouter_produit(product);
        cartRepository.save(cart);
    }


        public List<CartItem> voir_panier(Long cartId) {
            Cart cart = cartRepository.findByIdWithItems(cartId);
            if (cart == null) {
                return null;
            }
            return cart.getItems();
        }



    public double total_panier(Long cartId) {
        double total = 0;
        Cart cart = cartRepository.findByIdWithItems(cartId);
        if (cart == null) {
            return 0;
        }
        for (CartItem cartItem : cart.getItems()) {
            total += cartItem.getProduct().getPrix() * cartItem.getQuantite();
        }


        return total;
    }

    public void delete_panier(long id) {
        Optional<CartItem> optional = cartitemrepository.findById(id);
        if (optional.isPresent()) {
            cartitemrepository.delete_par_id(id);
        }


    }



}
