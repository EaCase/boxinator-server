package com.example.boxinator.models.account;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Optional;

public enum AccountType {
    GUEST, REGISTERED_USER, ADMIN;

    public static Optional<AccountType> getAccountType(Collection<GrantedAuthority> collection) {
        for (GrantedAuthority auth : collection) {
            switch (auth.getAuthority()) {
                case "ROLE_user" -> {
                    return Optional.of(AccountType.REGISTERED_USER);
                }
                case "ROLE_admin" -> {
                    return Optional.of(AccountType.ADMIN);
                }
                default -> {
                    // Not account type claim
                }
            }
        }
        return Optional.empty();
    }
}