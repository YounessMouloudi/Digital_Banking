package com.projects.digital_bank_backend.dtos;

import com.projects.digital_bank_backend.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;          // le Montant
    private OperationType type;
    private String description;
}
