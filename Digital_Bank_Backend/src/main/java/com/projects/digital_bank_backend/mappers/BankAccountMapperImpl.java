package com.projects.digital_bank_backend.mappers;

import com.projects.digital_bank_backend.dtos.*;
import com.projects.digital_bank_backend.entities.AccountOperation;
import com.projects.digital_bank_backend.entities.CurrentAccount;
import com.projects.digital_bank_backend.entities.Customer;
import com.projects.digital_bank_backend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/* hna had lmapper howa bach ghadi n7awlo données mn DTO l entities aw l3akss ay kol class ghatkon 3ando 2 methodes
wahda tat7awal lina les données dial un obj DTO l un objet 3adi w lakhra tadir l3akss ay mn objet 3adi l obj DTO
*/
@Service
public class BankAccountMapperImpl {

    /* hna ghadi ghansefto biha les données dial un obj customer l un obj customerDTO */
    public CustomerDTO fromCustomer(Customer customer) {

        CustomerDTO customerDTO = new CustomerDTO();

        /* spring 3tana had class BeanUtils w li fih had la méthode w li hia li tadir lina dik set(get) tan3tiwha
        la source mnin ghansefto l fin ghansefto par ex : hna la source howa customer ay howa li ghansefto mno
        les données l customerDTO mais kayn des autre frameworks li taydiro had lkhadma b7al MapStruct mais
        maghadich nkhadmo bih db */
        BeanUtils.copyProperties(customer,customerDTO);

        // hadi tari9a l3adia ay tadir set(get)
//        customerDTO.setId(customer.getId());
//        customerDTO.setName(customer.getName());
//        customerDTO.setEmail(customer.getEmail());

        return customerDTO;
    }

    /* hna ghadi ghansefto biha les données dial un obj customerDTO l un obj customer */
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {

        Customer customer = new Customer();
        /* hna la source howa customerDTO ay howa li ghansefto mno les données l customer */
        BeanUtils.copyProperties(customerDTO,customer);

        // hadi tari9a l3adia ay tadir set get
//        customer.setId(customerDTO.getId());
//        customer.setName(customerDTO.getName());
//        customer.setEmail(customerDTO.getEmail());

        return customer;

    }

    public SavingBankAccountDTO fromSavingAccount(SavingAccount savingAccount){

        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();

        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        /* hna ghadi ntransféréw mn savingAccount l savingDTO mais rah maghadich ysseft l customer dakchi
           3lach ghadi ndiro had ligne tania bach nsefto l customer tahowa ay ghadi nakhdo obj customer
           dial savingAccount w n7awloh l customerDto b la methode fromCustomer w n affectiwh l savingDTO
         */
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());

        return savingBankAccountDTO;
    }
    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){

        SavingAccount savingAccount = new SavingAccount();

        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        /* hna ghadi ndiro l3akss ay ghadi nssefto l obj customerDTO dial savingDTO w n7awloh l customer b from
        w n affectiwh l savingAccount */
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));

        return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount currentAccount){

        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();

        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());

        return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){

        CurrentAccount currentAccount = new CurrentAccount();

        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {

        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);

        return accountOperationDTO;
    }

    public AccountOperation fromAccountOperationDTO(AccountOperationDTO accountOperationDTO) {

        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO,accountOperation);

        return accountOperation;
    }
}
