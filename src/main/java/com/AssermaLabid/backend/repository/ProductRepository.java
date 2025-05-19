package com.AssermaLabid.backend.repository;

import com.AssermaLabid.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    long count();
    @Query("Select count(p) from Product p where p.stock=0")
    long ChercherProduitNull();
    @Query("SELECT p FROM Product p WHERE p.stock > 0 ORDER BY p.stock ASC")
    List<Product> findAllParOrder();

}
