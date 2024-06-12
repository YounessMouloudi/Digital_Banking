package com.projects.digital_bank_backend.services;

import com.projects.digital_bank_backend.entities.BankAccount;
import com.projects.digital_bank_backend.entities.CurrentAccount;
import com.projects.digital_bank_backend.entities.SavingAccount;
import com.projects.digital_bank_backend.repositories.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class BankService {

    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        // db mnin drna hadchi f service ghadi nbadlo l'attribut fetch w nradoha Lazy hit db ghadi tjib les operations

        BankAccount bankAccount = bankAccountRepository.findById("53ddb6a5-b1a6-4c71-86f1-d238175b6c94")
                .orElse(null);

        if(bankAccount != null) {
            System.out.println("*************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getFirstName() + bankAccount.getCustomer().getLastName());
            System.out.println(bankAccount.getClass().getSimpleName());

            if(bankAccount instanceof CurrentAccount){
                System.out.println("OVER-DRAFT"+((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("RAT"+((SavingAccount) bankAccount).getInterestRate());
            }

            bankAccount.getAccountOperations().forEach( operation -> {
                System.out.println( operation.getType()+ "\t" + operation.getAmount()+ "\t" +
                        operation.getOperationDate());
            });
        }

    }
}
