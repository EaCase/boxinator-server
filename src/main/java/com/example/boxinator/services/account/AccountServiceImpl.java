package com.example.boxinator.services.account;

import com.example.boxinator.dtos.account.AccountPostDto;
import com.example.boxinator.dtos.auth.AuthRegister;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.repositories.account.AccountRepository;
import com.example.boxinator.repositories.country.CountryRepository;
import com.example.boxinator.services.email.EmailService;
import com.example.boxinator.utils.DateTimeUtils;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    private final static String TEMP_ACCOUNT_PREFIX = "TEMP_";
    private final AccountRepository accountRepository;
    private final CountryRepository countryRepository;
    private final EmailService emailService;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            CountryRepository countryRepository,
            EmailService emailService
    ) {
        this.accountRepository = accountRepository;
        this.countryRepository = countryRepository;
        this.emailService = emailService;
    }

    @Override
    public Account register(AuthRegister info, String providerId) {
        if (info.getRegistrationToken() != null) {
            return completeTemporaryAccountRegistration(info, providerId);
        }
        Account acc = populateAccountFields(new Account(), info);
        acc.setEmail(info.getEmail());
        acc.setProviderId(providerId);
        acc.setCreatedAt(DateTimeUtils.now());
        return accountRepository.save(acc);
    }

    private Account completeTemporaryAccountRegistration(AuthRegister info, String providerId) {
        var temp = accountRepository.findAccountByProviderId(TEMP_ACCOUNT_PREFIX + info.getRegistrationToken()).orElseThrow(() ->
                new ApplicationException("Invalid registration token provided.", HttpStatus.BAD_REQUEST)
        );
        var acc = populateAccountFields(temp, info);
        acc.setProviderId(providerId);
        return accountRepository.save(acc);
    }

    // Sets all but email/ids/timestamp
    private Account populateAccountFields(Account acc, AuthRegister info) {
        if (info.getDateOfBirth() != null) {
            acc.setDob(DateTimeUtils.fromString(info.getDateOfBirth()));
        }
        if (info.getCountryId() != null) {
            acc.setCountry(countryRepository.findById(Long.valueOf(info.getCountryId())).orElseThrow(() ->
                    new ApplicationException("Invalid country id provided.", HttpStatus.BAD_REQUEST)));
        }
        acc.setFirstName(info.getFirstName());
        acc.setLastName(info.getLastName());
        acc.setContactNumber(info.getContactNumber());
        acc.setZipCode(info.getZipCode());
        return acc;
    }

    @Override
    public Account edit(AccountPostDto dto, String providerId) {
        var acc = accountRepository.findAccountByProviderId(providerId).orElseThrow(() ->
                new ApplicationException("Could not find the account.", HttpStatus.INTERNAL_SERVER_ERROR)
        );
        acc.setDob(DateTimeUtils.fromString(dto.getDateOfBirth()));
        acc.setFirstName(dto.getFirstName());
        acc.setLastName(dto.getLastName());
        acc.setZipCode(dto.getZipCode());
        acc.setContactNumber(dto.getContactNumber());
        acc.setCountry(countryRepository.findById(Long.valueOf(dto.getCountryId())).orElseThrow(() ->
                new ApplicationException("Invalid country id provided.", HttpStatus.BAD_REQUEST)));

        return accountRepository.save(acc);
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new ApplicationException(
                        "Could not find an account in the database matching the account id '" + id + "'.",
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    @Override
    public Account getByProviderId(String providerId) {
        return accountRepository.findAccountByProviderId(providerId).orElseThrow(() ->
                new ApplicationException(
                        "Could not find an account in the database matching the provider id '" + providerId + "'.",
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.findAccountByEmail(email).orElseThrow(() -> new ApplicationException(
                "Account with the provided email address was not found.",
                HttpStatus.BAD_REQUEST
        ));
    }

    @Override
    public Account registerTempAccount(String email) {
        accountRepository.findAccountByEmail(email).ifPresent(it -> {
            throw new ApplicationException("This email is already in use.", HttpStatus.BAD_REQUEST);
        });

        Account acc = new Account();
        // Set a random uuid as the providerId for the account, which can later be used when
        // the account gets fully registered. (+ prefix to clean up these accounts later if needed.)
        var token = UUID.randomUUID();
        acc.setProviderId(TEMP_ACCOUNT_PREFIX + token);
        acc.setEmail(email);
        acc.setCreatedAt(DateTimeUtils.now());
        accountRepository.save(acc);
        emailService.sendAccountRegistration(email, token.toString());
        return acc;
    }

    @Override
    public AccountStatus getAccountStatus(String email) {
        var result = accountRepository.findAccountByEmail(email);

        if (result.isPresent()) {
            var account = result.get();
            var isTemporary = account.getProviderId().contains(TEMP_ACCOUNT_PREFIX);
            if (isTemporary) {
                return AccountStatus.TEMPORARY;
            }
            return AccountStatus.REGISTERED;
        }

        return AccountStatus.DOES_NOT_EXIST;
    }

    @Override
    @Transactional
    public void deleteByProviderId(String id) {
        System.out.println("Service delete " + id);
        accountRepository.deleteAccountByProviderId(id);
    }

    @Override
    public boolean emailMatchesToken(String email, String registrationToken) {
        var acc = accountRepository.findAccountByProviderId(TEMP_ACCOUNT_PREFIX + registrationToken).orElseThrow(() ->
                new ApplicationException("Invalid registration token.", HttpStatus.BAD_REQUEST)
        );
        return acc.getEmail().equals(email);
    }
}
