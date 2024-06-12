package com.projects.digital_bank_backend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // hna wakha drna entity maghadich ycréé lina hada comme table hit JPA 3arf bila strategy li drna hia Single Table
@DiscriminatorValue("CA") // hadi hia lvalue li ghatzad f champ Type mnin ghancréyiw un objet CurrentAccount
@Data @AllArgsConstructor @NoArgsConstructor
public class CurrentAccount extends BankAccount {

    private double overDraft; // découvert ay ch7al l7ad l'a9ssa li n9dar nss7ab mno
}
