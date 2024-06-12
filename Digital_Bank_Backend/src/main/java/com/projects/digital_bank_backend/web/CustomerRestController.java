package com.projects.digital_bank_backend.web;

import com.projects.digital_bank_backend.dtos.BankAccountDTO;
import com.projects.digital_bank_backend.dtos.CustomerDTO;
import com.projects.digital_bank_backend.entities.Customer;
import com.projects.digital_bank_backend.exceptions.CustomerNotFoundException;
import com.projects.digital_bank_backend.services.BankAccountService;
import com.projects.digital_bank_backend.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor @Slf4j
@CrossOrigin("http://localhost:4200/")
/* had CrossOrigin hia bach tan3tiw l'authorisation l des app bach ymkan lihom yakhdo les données mn had l'application
hna f had l7ala dayrin front b angular alors bach nakhdo les données mn RestController darori khass ndiro hadi w
na3tiwha url dial app sinon ila madrnahach wakha nsta3mlo l'url d l backend f front maymaknch yakhod les données hit
had crossOrigin tatblokih => ila drna * tat3ni tan3tiw authorisations l ga3 les urls  */
public class CustomerRestController {

    private BankAccountService bankAccountService;
    private CustomerService customerService;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerDTO> customers() { return customerService.listCustomers();}

    @GetMapping("/customers/search")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword) {
        return customerService.searchCustomers(keyword);
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDTO getCustomer(@PathVariable("id") Long customerId) throws CustomerNotFoundException {
        return customerService.getCustomer(customerId) ;
    }

    @PostMapping("/customers")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public CustomerDTO updateCustomer(@RequestBody CustomerDTO customerDTO,@PathVariable Long customerId) throws CustomerNotFoundException {
        customerDTO.setId(customerId);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{customerId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PostMapping("/customer/profile")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<BankAccountDTO> getCustomerProfile(@RequestBody String email) throws CustomerNotFoundException {

        CustomerDTO customerDTO = customerService.getCustomerProfile(email);

        List<BankAccountDTO> bankAccounts = bankAccountService.customerBankAccount(customerDTO.getId());

        return bankAccounts;
    }

    @PostMapping("/update/password")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public void updatePassword(@RequestBody Map<String, String> request) throws CustomerNotFoundException {
        String email = request.get("email");
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

//        System.out.println(email+" "+oldPassword+" "+newPassword);
        customerService.updatePassword(email,oldPassword,newPassword);
    }
}
