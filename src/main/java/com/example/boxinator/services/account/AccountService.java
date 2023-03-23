package com.example.boxinator.services.account;

import com.example.boxinator.dtos.account.AccountPostDto;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.services.shared.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    /**
     * Register a temporary account with an email address, and send a link to the address which can be used to
     * complete the registration process.
     */
    Account registerTempAccount(String email);

    /**
     * Register a new account. If registrationToken is provided with the info object, replaces the matching temp
     * account in the database.
     *
     * @return Account
     */
    Account register(AuthRegister info, String providerId);

    Account getById(Long id);

    Account getByProviderId(String providerId);

    Account getByEmail(String email);

    Account edit(AccountPostDto dto, String providerId);


    /**
     * Get the registration status of an account with its email address.
     *
     * @param email of the account
     * @return AccountStatus
     */
    AccountStatus getAccountStatus(String email);


    void deleteByProviderId(String id);

    boolean emailMatchesToken(String email, String registrationToken);

    /**
     * Enum to indicate the registration status of an account internally.
     */
    enum AccountStatus {
        DOES_NOT_EXIST,
        TEMPORARY,
        REGISTERED
    }
}
