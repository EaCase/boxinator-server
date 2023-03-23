package com.example.boxinator.utils;

import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.account.AccountType;
import com.example.boxinator.services.account.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthUtils {
    private AuthUtils() {
    }

    public static Long getUserId(AccountService accountService, Authentication auth) {
        var jwt = (Jwt) auth.getPrincipal();
        Account account = accountService.getByProviderId(jwt.getSubject());
        return account.getId();
    }

    public static boolean isAdmin(Authentication auth) {
        AccountType accountType = AccountType.getAccountType(auth.getAuthorities());
        return accountType == AccountType.ADMIN;
    }
}
