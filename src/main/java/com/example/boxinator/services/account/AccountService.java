package com.example.boxinator.services.account;

import com.example.boxinator.dtos.account.AccountPostDto;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.models.account.Account;

/**
 * Handles the accounts data in the servers' database. Is not capable of handling user credentials, and does not fully
 * handle users on its own.
 * <p>
 * All registration methods here should be called from a service which can handle registration with credentials.
 * The id which is given to the user in the other service, is handled as providerId here, and is used to map the two together
 * (Properly authorized/handled account credentials - Other account data: dob etc.).
 */
public interface AccountService {
    /**
     * Register a temporary account with an email address, and send a link to the address which can be used to
     * complete the registration process.
     */
    Account registerTempAccount(String email);

    /**
     * Register a new account. If registrationToken is provided with the AuthRegister object, replaces the matching temp
     * account in the database.
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

    /**
     * Check whether an email address matches the one used when the provided registration token was created.
     */
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
