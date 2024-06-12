package com.projects.digital_bank_backend;

import com.projects.digital_bank_backend.dtos.BankAccountDTO;
import com.projects.digital_bank_backend.dtos.CurrentBankAccountDTO;
import com.projects.digital_bank_backend.dtos.CustomerDTO;
import com.projects.digital_bank_backend.dtos.SavingBankAccountDTO;
import com.projects.digital_bank_backend.entities.*;
import com.projects.digital_bank_backend.enums.AccountStatus;
import com.projects.digital_bank_backend.enums.OperationType;
import com.projects.digital_bank_backend.exceptions.BalanceNotSuffisantException;
import com.projects.digital_bank_backend.exceptions.BankAccountNotFoundException;
import com.projects.digital_bank_backend.exceptions.CustomerNotFoundException;
import com.projects.digital_bank_backend.repositories.*;
import com.projects.digital_bank_backend.services.BankAccountService;
import com.projects.digital_bank_backend.services.BankService;
import com.projects.digital_bank_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankBackendApplication.class, args);
    }

//    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService, UserService userService){
        return args -> {

//            Stream.of("Amine","Imad","Sara").forEach( name -> {
//                CustomerDTO customer = new CustomerDTO();
//                customer.setName(name);
//                customer.setEmail(name+"@gmail.com");
//
//                bankAccountService.saveCustomer(customer);
//            });

//            bankAccountService.listCustomers().forEach( customer -> {
//                try {
//                    bankAccountService.saveCurrentBankAccount(Math.random() * 50000,9000, customer.getId());
//                    bankAccountService.saveSavingBankAccount(Math.random() * 90000,5.5, customer.getId());
//
//                } catch (CustomerNotFoundException e) {
//                    e.printStackTrace();
//                }
                /* kayna wahd tari9a : 3iwad manb9aw ndiro catch mora catch b7al l'exp 1 n9adro njam3ohom f catch
                   wehda b7al li f exp 2

                    exp 1 :
                    catch (CustomerNotFoundException e) {
                        e.printStackTrace();
                    } catch (BalanceNotSuffisantException e) {
                        e.printStackTrace();
                    } catch (BankAccountNotFoundException e) {
                        e.printStackTrace();
                    }

                    exp 2 :
                    catch (CustomerNotFoundException | BankAccountNotFoundException | BalanceNotSuffisantException e) {
                        e.printStackTrace();
                    }

                */
//            });

//             hna hadi mab9atch khedama hit badelna BankAccount b BankAccountDto

//             List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
//
//             for (BankAccount account:bankAccounts) {
//
//                 // hna ghandiro for bach chaque bankAccount ghaykon 3ando 4 operations
//                 for (int i = 0; i < 2; i++) {
//                     bankAccountService.credit(account.getId(),3000+Math.random() * 10000,"Credit" );
//                     System.out.println("Credit operation => "+ account.getId());
//
//                     bankAccountService.debit(account.getId(),1000+Math.random() * 9000,"Debit" );
//                     System.out.println("Debit operation => "+ account.getId());
//
//                 }
//             };

//            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();

//            for (BankAccountDTO account:bankAccounts) {
//
//                // hna ghandiro for bach chaque bankAccount ghaykon 3ando 4 operations
//                for (int i = 0; i < 2; i++) {
//                    String accountId;
//
//                    if(account instanceof SavingBankAccountDTO){
//                        accountId = ((SavingBankAccountDTO) account).getId();
//                    }
//                    else {
//                        accountId = ((CurrentBankAccountDTO) account).getId();
//                    }
//
//                    bankAccountService.credit(accountId,3000+Math.random() * 10000,"Credit" );
//                    System.out.println("Credit operation => "+ accountId);
//
//                    bankAccountService.debit(accountId,1000+Math.random() * 9000,"Debit" );
//                    System.out.println("Debit operation => "+ accountId);
//                }
//            }

            Role roleUser = userService.addNewRole("USER");
            Role roleAdmin = userService.addNewRole("ADMIN");

//            userService.addNewUser("user","user@gmail.com",passwordEncoder().encode("aaaa"), Set.of(roleUser));
            userService.addNewUser("admin","admin@gmail.com",passwordEncoder().encode("aaaaa"),Set.of(roleUser,roleAdmin));

        };
    };

//    @Bean // hna hadchi li drna f lewal créyina les données b tari9a normal ghir b reposit ama lfo9 khdmna b Service
//    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
//                                        BankAccountRepository bankAccountRepository,
//                                        AccountOperationRepository accountOperationRepository ){
//        return args -> {
//            Stream.of("ali","tarik","sara").forEach( name -> {
//                Customer customer = new Customer();
//                customer.setName(name);
//                customer.setEmail(name + "@gmail.com");
//
//                customerRepository.save(customer);
//            });
//
//            customerRepository.findAll().forEach( customer -> {
//
//                CurrentAccount currentAccount = new CurrentAccount();
//                currentAccount.setId(UUID.randomUUID().toString());
//                currentAccount.setBalance(Math.random() * 70000);
//                currentAccount.setCreatedAt(new Date());
//                currentAccount.setStatus(AccountStatus.CREATED);
//                currentAccount.setCustomer(customer);
//                currentAccount.setOverDraft(9000);
//
//                bankAccountRepository.save(currentAccount);
//
//                SavingAccount savingAccount = new SavingAccount();
//                savingAccount.setId(UUID.randomUUID().toString());
//                savingAccount.setBalance(Math.random() * 70000);
//                savingAccount.setCreatedAt(new Date());
//                savingAccount.setStatus(AccountStatus.CREATED);
//                savingAccount.setCustomer(customer);
//                savingAccount.setInterestRate(4.5);
//
//                bankAccountRepository.save(savingAccount);
//
//            });
//
//            bankAccountRepository.findAll().forEach( account -> {
//
//                // hna ghandiro for bach chaque Account ghaykon 3ando 5 operations
//                for (int i = 0; i < 5; i++) {
//
//                    AccountOperation accountOperation = new AccountOperation();
//                    accountOperation.setAmount(Math.random() * 1000);
//                    accountOperation.setOperationDate(new Date());
//                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
//                    accountOperation.setBankAccount(account);
//
//                    accountOperationRepository.save(accountOperation);
//                }
//
//            });
//        };
//    }

}
