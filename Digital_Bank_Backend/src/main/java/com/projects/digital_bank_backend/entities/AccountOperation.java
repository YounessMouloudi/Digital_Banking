package com.projects.digital_bank_backend.entities;

import com.projects.digital_bank_backend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AccountOperation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount; // le Montant

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @ManyToOne // plusieurs operations appartient a un seul compte
    private BankAccount bankAccount;

    private String description;
}
