package com.projects.digital_bank_backend.dtos;

import lombok.Data;

@Data
public class VirementDTO {
    private String accountIdSource;
    private String accountIdDestination;
    private double amount;
}
