package com.example.boxinator.services.account;

import com.example.boxinator.dtos.account.AccountPostDto;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.services.shared.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    Account register(AuthRegister info, String providerId);

    Account edit(AccountPostDto dto, String providerId);

    Account getById(Long id);

    Account getByProviderId(String providerId);

    void deleteByProviderId(String id);
}
