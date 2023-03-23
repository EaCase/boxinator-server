package com.example.boxinator.repositories.account;

import com.example.boxinator.models.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByProviderId(String providerId);

    Optional<Account> findAccountByEmail(String email);

    void deleteAccountByProviderId(String id);
}
