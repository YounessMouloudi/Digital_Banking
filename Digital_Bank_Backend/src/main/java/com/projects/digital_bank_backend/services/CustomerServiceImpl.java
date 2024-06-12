package com.projects.digital_bank_backend.services;

import com.projects.digital_bank_backend.dtos.CustomerDTO;
import com.projects.digital_bank_backend.entities.Customer;
import com.projects.digital_bank_backend.entities.Role;
import com.projects.digital_bank_backend.exceptions.CustomerNotFoundException;
import com.projects.digital_bank_backend.mappers.BankAccountMapperImpl;
import com.projects.digital_bank_backend.repositories.CustomerRepository;
import com.projects.digital_bank_backend.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private BankAccountMapperImpl dtoMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    // Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    // Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /* had logger c'est pour la jounalisation ay bach t afficher un message wla un erreur wla un bug ....
       ima tadiro b had tari9a aw tadiro b l'annotation @Slf4j dial Lombok mais ila dertih b tari9a normal maghadich
       yekhdam lik ila konti dayr l'annotation @AllArgsConstructor hit tay3ti wahd erreur ay ya3ni khass dir ghir
       @Autowired bach tcréé le constructeur avec param pour les repositories => donc dakchi 3lach ghadi nkhadmo
       ghir b annotation
     */

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();

        /* hadi tatssma la programmation impérative càd hadi hia la méthode classique bach ghadi n mappiw dakchi
        w nraj3o une liste de customerDto */
//        List<CustomerDTO> customerDTOS = new ArrayList<>();
//        for(Customer customer:customers) {
//            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
//            customerDTOS.add(customerDTO);
//        }
//        return customerDTOS;


        // hna ghadi ndiro la programmation fonctionnel càd on utilise les streams li ghayssahlo 3lina n mapiw
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());

        return customerDTOS;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {

        List<Customer> customers = customerRepository.searchCustomer(keyword);

        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());

        return customerDTOS;
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");

        if(customerRepository.findByEmail(customerDTO.getEmail()) != null) {
            throw new IllegalArgumentException("this email already exists");
        }
        else if(customerRepository.findByCin(customerDTO.getCin()) != null) {
            throw new IllegalArgumentException("this cin already exists");
        };

        /* hna ghadi n7awlo customerDto li ghayjin mn request l customer normal aprés ghadi n enregistriwh
        aprés ghadi ntrasformiwh l un customerDto bach yban f l'affichage*/
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);

        String generatePassword = generateRandomPassword();
        customer.setPassword(passwordEncoder.encode(generatePassword));
        customer.setUsername(customer.getFirstName()+" "+customer.getLastName());

        Role role = roleRepository.findByRole("USER");

        if(role == null) throw new RuntimeException("role USER not found");
        customer.getRoles().add(role);

        Customer savedCustomer = customerRepository.save(customer);
        CustomerDTO responseCustomerDTO = dtoMapper.fromCustomer(savedCustomer);
        // hna ghadi nsefto l password li generinah par déf bach na3tiwh l user
        responseCustomerDTO.setPassword(generatePassword);

        return responseCustomerDTO;
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException {
        log.info("Updating Customer");

        /* hna si je utilise dtoMapper bach n7awl customerDto l un customer f update darori kan tay khassni
        ndir update l password w username w role wla taywaliw null ila madarthomch c'est pour ça
        maghadich nssta3mlo

        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        */
        Customer customer = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setCin(customerDTO.getCin());
        customer.setAddress(customerDTO.getAddress());

        if(customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        }

        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer Not Found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public String generateRandomPassword() {

        // Méthode 1
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int PASSWORD_LENGTH = 5;
        SecureRandom RANDOM = new SecureRandom();

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();

        // Méthode 2
        // return UUID.randomUUID().toString().replace("-", "").substring(0, 5);
    }

    @Override
    public CustomerDTO getCustomerProfile(String email) throws CustomerNotFoundException {

        Customer customer = customerRepository.findByEmail(email);

        if(customer == null) throw new CustomerNotFoundException("Customer With This Email Not Found");

        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public void updatePassword(String email, String oldPassword, String newPassword) throws CustomerNotFoundException {

        Customer customer = customerRepository.findByEmail(email);

        if(customer == null) throw new CustomerNotFoundException("Customer With This Email Not Found");

//        System.out.println(passwordEncoder.matches(oldPassword,customer.getPassword()));

        if(!passwordEncoder.matches(oldPassword,customer.getPassword())) {
            throw new IllegalArgumentException("The Old Password Doesn't Match The Password You Entered");
        }
        else {
            customer.setPassword(passwordEncoder.encode(newPassword));
        }

        customerRepository.save(customer);
    }


}
