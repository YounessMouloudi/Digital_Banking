package com.projects.digital_bank_backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Customer extends User{
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String name;

    private String firstName;
    private String lastName;

    @Size(min = 10,max = 10)
    private String phone;

    @Column(unique = true)
    @Size(min = 6,max = 8)
    private String cin;
    private String address;

//    @DateTimeFormat(pattern = "yyyy-mm-dd")
//    private Date dateNaissance;


    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL) // un client peut avoir plusieurs comptes
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    /* hna hadi dranaha bach mnin yjib lia la listes dial customers mayjibch m3aha ta list dial BankAccount
    ay tangolo lih b had la methode mnin twssal l la liste des bankaccounts ignoriha ay en mot technique
     matandiroch liha la serialisation objet Json => had l'annotation dial Jackson */
    private List<BankAccount> bankAccounts;

    /* hna 3ndna l3ala9a OneToMany <-> ManyToOne bidirectionnelle ay ana id de customer ghaykon comme un clé étranger
     dans la table BankAccount mais darori khass nzidou hadik mappedBy bach jpa t3raf bila had le clé étranger c'est le
     meme clé qui représente cette relation hit ila madrenahch JPA une fois tatchof Onetomany aw ManytoOne ghatcréé
     un clé étranger f les 2 tables mais makhassch ykon haka hit c'est le meme clé donc dakchi 3lach khass nzido had
     mappedBy w ymkan dima had mappedBy tandiroha 3nd OneToMany hit had l class howa li ghanakhdo mno id w ndiroh
     comme clé étranger f l'autre class(table) li 3ndha ManyToOne
     */
}
