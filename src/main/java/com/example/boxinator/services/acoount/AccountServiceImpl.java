package com.example.boxinator.services.acoount;

import com.example.boxinator.dtos.account.AccountPostDto;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.repositories.account.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository ){
        this.accountRepository = accountRepository;
    }

    @Override
    public Account create(AccountPostDto dto) {
        return null;
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new ApplicationException(
                "Account with id: " + id + " could not be found",
                HttpStatus.NOT_FOUND
        ));
    }

    @Override
    public List<Account> getAll() {
        return null;
    }

    @Override
    public Long deleteById(Long id) {
        return null;
    }

    @Override
    public Account update(Long id, AccountPostDto dto) {
        return null;
    }
}
