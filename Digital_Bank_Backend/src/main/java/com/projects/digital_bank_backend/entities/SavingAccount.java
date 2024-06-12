package com.projects.digital_bank_backend.entities;

// SavingAccount howa Compte Epargne

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SA") // hadi hia lvalue li ghatzad f champ Type mnin ghancréyiw un objet SavingAccount
@Data @AllArgsConstructor @NoArgsConstructor
public class SavingAccount extends BankAccount {
    private double interestRate; // ay عر الفائدة
}
