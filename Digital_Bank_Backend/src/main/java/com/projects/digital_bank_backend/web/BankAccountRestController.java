package com.projects.digital_bank_backend.web;

import com.projects.digital_bank_backend.dtos.*;
import com.projects.digital_bank_backend.exceptions.BalanceNotSuffisantException;
import com.projects.digital_bank_backend.exceptions.BankAccountNotFoundException;
import com.projects.digital_bank_backend.exceptions.CustomerNotFoundException;
import com.projects.digital_bank_backend.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:4200/")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<AccountOperationDTO> getAccountHistory(@PathVariable String accountId) {
        return bankAccountService.accountsHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public AccountHistoryDTO getAccountHistoryPagination(@PathVariable String accountId,
                                                         @RequestParam(name = "page",defaultValue = "0") int page,
                                                         @RequestParam(name = "size",defaultValue = "2") int size)
                                                         throws BankAccountNotFoundException {

        return bankAccountService.accountsHistoryPagination(accountId,page,size);
    }

    @PostMapping("/accounts/debit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BalanceNotSuffisantException, BankAccountNotFoundException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/accounts/credit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public void transfer(@RequestBody VirementDTO virementDTO) throws BalanceNotSuffisantException, BankAccountNotFoundException {
//        System.out.println(virementDTO);
        this.bankAccountService.transfer(virementDTO.getAccountIdSource(), virementDTO.getAccountIdDestination(),
                virementDTO.getAmount());
    }

    @GetMapping("/accounts/customer/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<BankAccountDTO> getCustomerBankAccount(@PathVariable Long customerId) {
        return bankAccountService.customerBankAccount(customerId);

//        return bankAccountService.customerBankAccount(customerId).stream().map(account -> {
//                    Map<String, String> accountInfo = new HashMap<>();
//                    accountInfo.put("type", account.getType());
//                    return accountInfo;
//                })
//                .collect(Collectors.toList());

    }

    @PostMapping("/accounts/CurrentBankAccount/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CurrentBankAccountDTO saveCurrentBankAccount (@RequestParam(name = "initialBalance", defaultValue = "100")
                                                         double initialBalance,
                                                         @RequestParam(name = "overDraft", defaultValue = "10000")
                                                         double overDraft,
                                                         @PathVariable Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(initialBalance,overDraft,customerId);
    }

    @PostMapping("/accounts/SavingBankAccount/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public SavingBankAccountDTO saveSavingBankAccount (@RequestParam(name = "initialBalance", defaultValue = "0")
                                                       double initialBalance,
                                                       @RequestParam(name = "interestRate", defaultValue = "0")
                                                       double interestRate,
                                                       @PathVariable Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveSavingBankAccount(initialBalance,interestRate,customerId);
    }
}
