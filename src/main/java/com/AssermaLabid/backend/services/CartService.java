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


    public void ajouter_panier(int produitId, User user) {
        Product product = productService.getProductById(produitId);

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            user.setCart(cart);
            cartRepository.save(cart);
        }

        // Chargement des éléments du panier depuis la base
        List<CartItem> cartItemList = cartRepository.getCartItemsByCartId(cart.getId());

        CartItem existingItem = null;
        for (CartItem item : cartItemList) {
            if (item.getProduct().getId() == produitId) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            existingItem.setQuantite(existingItem.getQuantite() + 1);
            cartitemrepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantite(1);
            cartitemrepository.save(newItem);
        }
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
    @Transactional
    public void acheter_panier(User user) {

        Cart cart = user.getCart();
        List<CartItem> cartItemList = cartRepository.getCartItemsByCartId(cart.getId());

        for (CartItem item : cartItemList) {
            cartitemrepository.delete(item);
        }

        cart.getItems().clear(); // vider la liste côté objet Cart
        cartRepository.save(cart); // sauvegarder le Cart sans items
    }




}
