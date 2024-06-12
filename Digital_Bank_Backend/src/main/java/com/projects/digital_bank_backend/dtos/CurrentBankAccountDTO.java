package com.projects.digital_bank_backend.dtos;

import com.projects.digital_bank_backend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO{

    private String id;

    private double balance;

    private Date createdAt;

    private AccountStatus status;

    private CustomerDTO customerDTO;

    private double overDraft;
}