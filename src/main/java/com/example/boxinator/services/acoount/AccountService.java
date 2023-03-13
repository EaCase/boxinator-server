package com.example.boxinator.services.acoount;

import com.example.boxinator.dtos.account.AccountPostDto;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.services.shared.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface AccountService extends CrudService<Account, AccountPostDto> {

}
