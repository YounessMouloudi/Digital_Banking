package com.projects.digital_bank_backend.services;

import com.projects.digital_bank_backend.dtos.*;
import com.projects.digital_bank_backend.entities.*;
import com.projects.digital_bank_backend.enums.OperationType;
import com.projects.digital_bank_backend.exceptions.BalanceNotSuffisantException;
import com.projects.digital_bank_backend.exceptions.BankAccountNotFoundException;
import com.projects.digital_bank_backend.exceptions.CustomerNotFoundException;
import com.projects.digital_bank_backend.mappers.BankAccountMapperImpl;
import com.projects.digital_bank_backend.repositories.AccountOperationRepository;
import com.projects.digital_bank_backend.repositories.BankAccountRepository;
import com.projects.digital_bank_backend.repositories.CustomerRepository;
import com.projects.digital_bank_backend.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    // Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    // Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /* had logger c'est pour la jounalisation ay bach t afficher un message wla un erreur wla un bug ....
       ima tadiro b had tari9a aw tadiro b l'annotation @Slf4j dial Lombok mais ila dertih b tari9a normal maghadich
       yekhdam lik ila konti dayr l'annotation @AllArgsConstructor hit tay3ti wahd erreur ay ya3ni khass dir ghir
       @Autowired bach tcréé le constructeur avec param pour les repositories => donc dakchi 3lach ghadi nkhadmo
       ghir b annotation
     */


    /* mhm rah hna f les methodes awal haja 9bal matbda darori khass tdir des régles métiers ay des verifications
       et des tests par ex : tchof wach le client déja existe wach le client f black liste, tester 3la un compte
       wach kayn aw ina type .... ect => had les régles métiers tat7adedhom société
       mais hna ghadi nkhademo normal sans régles
    */

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();

        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOS;
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer == null) {
            // hna had l'exception tatssma Exception Metier ay nta li tadirha w matatb9ach ta3tamed ghi 3la RunTimeEcxception
            throw new CustomerNotFoundException("Customer not Found");
        }
        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);

        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);

        return dtoMapper.fromCurrentAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if(customer == null) {
            throw new CustomerNotFoundException("Customer not Found");
        }
        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);

        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);

        return dtoMapper.fromSavingAccount(savedBankAccount);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("BankAccount Not Found"));

        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingAccount(savingAccount);
        }
        else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public List<AccountOperationDTO> accountsHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);

        List<AccountOperationDTO> accountOperationDTOS = accountOperations.stream().map( accountOperation -> {
            return dtoMapper.fromAccountOperation(accountOperation);
        }).collect(Collectors.toList());

//        return accountOperations.stream().map(accountOperation -> dtoMapper.fromAccountOperation(accountOperation))
//                .collect(Collectors.toList());

        return accountOperationDTOS;
    }

    @Override
    public AccountHistoryDTO accountsHistoryPagination(String accountId, int page, int size) throws BankAccountNotFoundException {

        if (accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be empty");
        }

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);

        if(bankAccount == null) {
            throw new BankAccountNotFoundException("Account Not Found");
        }

        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(
                accountId, PageRequest.of(page,size) );

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream()
                .map(operation -> dtoMapper.fromAccountOperation(operation))
                .collect(Collectors.toList());

        // hadchi on peut fair comme ça ou avec mapper
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException,
            BalanceNotSuffisantException {

        /* BankAccount bankAccount = getBankAccount(accountId);
           hna kona tan3aytto la methode getBankAccount() hit tatraja3 lina un obj de bankAccount mais aprés mnin
           modifina f bankAccount w wlat tatraja3 lina bankaccountDTO donc la méthode mab9atch sal7a c'est pour ça
           ghadi n3ayto ghir l repository
        */
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("BankAccount Not Found"));

        if(bankAccount.getBalance() < amount)
            throw new BalanceNotSuffisantException("Balance not Sufficient");

        AccountOperation accountOperation = new AccountOperation();

        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFoundException("BankAccount Not Found"));

        AccountOperation accountOperation = new AccountOperation();

        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);

        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSuffisantException, BankAccountNotFoundException {

        BankAccount bankAccountSource = bankAccountRepository.findById(accountIdSource).orElseThrow(() ->
                new BankAccountNotFoundException("Account Source Not Found"));

        BankAccount bankAccountDestination = bankAccountRepository.findById(accountIdDestination).orElseThrow(() ->
                new BankAccountNotFoundException("Account Destination Not Found"));

        debit(bankAccountSource.getId(),amount,"Transfer to "+bankAccountDestination.getId());
        credit(bankAccountDestination.getId(),amount,"Transfer from "+bankAccountSource.getId());
    }

    @Override
    public List<BankAccountDTO> customerBankAccount(Long customerId) {

//        List<BankAccount> bankAccounts = bankAccountRepository.findAllByCustomerId(customerId);

        List<BankAccount> bankAccounts = bankAccountRepository.findBankAccountsByCustomer_Id(customerId);

        List<BankAccountDTO> customerBankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount;
                return dtoMapper.fromSavingAccount(savingAccount);
            } else if(bankAccount instanceof CurrentAccount) {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentAccount(currentAccount);
            } else {
                try {
                    throw new BankAccountNotFoundException("Account Not Found");
                } catch (BankAccountNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).collect(Collectors.toList());
//        List<Map<String, String>> customerBankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
//            Map<String, String> accountInfo = new HashMap<>();
//            accountInfo.put("type", bankAccount.getType());
//            accountInfo.put("id", bankAccount.getId());
//            return accountInfo;
//        }).collect(Collectors.toList());
//        System.out.println("customer "+customerBankAccountDTOS);

        // hna drna sort dial Bank Accounts by type mn backend
        Collections.sort(customerBankAccountDTOS, Comparator.comparing(BankAccountDTO::getType));

        return customerBankAccountDTOS;
    }
}
