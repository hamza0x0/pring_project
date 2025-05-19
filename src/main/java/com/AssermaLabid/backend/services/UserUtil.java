package com.AssermaLabid.backend.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.AssermaLabid.backend.models.User;

public class UserUtil {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal(); // Attention : ici, il faut que ton entité User implémente UserDetails
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
