package com.projects.digital_bank_backend.services;

import com.projects.digital_bank_backend.dtos.*;
import com.projects.digital_bank_backend.exceptions.BalanceNotSuffisantException;
import com.projects.digital_bank_backend.exceptions.BankAccountNotFoundException;
import com.projects.digital_bank_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    /* mhm rah hna f les methodes awal haja 9bal matbda darori khass tdir des régles métiers ay des verifications
       et des tests par ex : tchof wach le client déja existe wach le client f black liste, tester 3la un compte
       wach kayn aw ina type .... ect => had les régles métiers tat7adedhom société
       mais hna ghadi nkhademo normal sans régles
    */

    List<BankAccountDTO> bankAccountList();
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
            throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
            throws CustomerNotFoundException;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<AccountOperationDTO> accountsHistory(String accountId);
    AccountHistoryDTO accountsHistoryPagination(String accountId, int page, int size) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSuffisantException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination,double amount) throws BalanceNotSuffisantException, BankAccountNotFoundException;
    List<BankAccountDTO> customerBankAccount(Long customerId);

}
