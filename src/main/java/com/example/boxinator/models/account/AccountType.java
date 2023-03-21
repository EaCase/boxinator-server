package com.example.boxinator.models.account;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Optional;

public enum AccountType {
    GUEST, REGISTERED_USER, ADMIN;

    public static AccountType getAccountType(Collection<? extends GrantedAuthority> collection) {
        for (GrantedAuthority auth : collection) {
            switch (auth.getAuthority()) {
                case "ROLE_user" -> {
                    return AccountType.REGISTERED_USER;
                }
                case "ROLE_admin" -> {
                    return AccountType.ADMIN;
                }
                default -> {
                    // Not account type claim
                }
            }
        }
        return AccountType.GUEST;
    }

    public static String asString(AccountType accountType) {
        return switch (accountType) {
            case ADMIN -> "admin";
            case REGISTERED_USER -> "user";
            case GUEST -> "guest";
        };
    }
}