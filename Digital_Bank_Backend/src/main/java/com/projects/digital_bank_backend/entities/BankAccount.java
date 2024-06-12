package com.projects.digital_bank_backend.entities;

import com.projects.digital_bank_backend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4) /* hna hadi bach tan7adedo dak champ li ghaytzad chno smito w ch7al
d les caracteres li maximum ghayakhod w ila ma7dedtich lih ghayakhod 255 par déf w kayn ta type mais par déf tayakhod
type String => ay hada ghan7ato fih type dial compte wach CurrentAccount aw SavingAccount */
@Data @AllArgsConstructor @NoArgsConstructor
public abstract class BankAccount {

    @Id
    private String id;

    private double balance; // solde

    private Date createdAt;

    @Enumerated(EnumType.STRING) /* hna drna String bach yakhod nafss les values li drna f Enum hit par déf tayakhod
    Ordinal ay 0,1,2,.... */
    private AccountStatus status;

    @ManyToOne // hna 3ndna l3ala9a Many to One ay un compte (BankAccount) concerne a un seul client (customer)
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.LAZY) // un compte peut avoir plusieurs Operations
    private List<AccountOperation> accountOperations;
    /* hna kona drna Eager bach yjib lia ta les operations hit ila madrnahach ghadi yjib ghir les données dial class
       ay maghadich yjib les données dial class Operation li 3ando relation m3ah mais had Eager fiha wahd l'inconvénient
       w li hia anaho y9dar yjib lia ga3 les données ay ychargé lia f la mémoire bcp des données w li maghadich
       nsta3mlhom w haka ghat9al la mémoire c'est pour ça ghadi nkhadmo ghir b FetchType.Lazy hia li tatkon par défault
       w hadi tatjib ghir les données d l class mais la bghiti tjib les données dial un autre class li 3ando relation
       m3ah une fois ghadi n3ayto 3la la fonction li 3ndha 3ala9a b les operations ghadi tjib lik les données ay tatjib
       les données ta tatehtajhom bach haka la mémoire tb9a khfifa mais had l9adia khass ta ndiro service 3ad tekhdam
       donc db ghankhadmeo ghi b Eager
    */

    // hadi zadtha bach ngénériw un id de type UUID pour currentAccount et saveAccount w li ghaykon manuellement
//    protected void generateId() {
//        this.id = UUID.randomUUID().toString();
//    }
}

/* dans POO il y a l'héritage mais dans BD Relationnelle il n'a pas d'héritage alors comment mapper l'héritage
=> il y a 3 stratégies :

    1 - Single Table : hada tay3ni ghadi ncréyiw juste 1 table de BankAccount w ghaykono fih les attributs dialo et
                       aussi les attributs dial les classes enfants ay li tay héritiw mn had l class w f had l class
                       ghatzad wahd champ li hia Type had Type ghaykon fih le nom dial class enfant ay b had champ
                       tan7adedo wach had compte mn naw3 class Current aw class Saving ila kan mn class Current alors
                       attribut dial class Saving ghatkon dima NULL w ila kan mn class Saving alors attribut dial class
                       Current ghatkon dima NULL.  =>  w hada howa li taykhadmo bih bzf bach maykatrouch les tables
                       hit plus performant

    2 - Table per class : hada tay3ni ghadi ndiro table pour chaque class Enfant ay f had l'exemple ghandiro 2 tables
                          table CurrentAccount ghaykon fih ga3 les attributs dial BankAccount w dialo howa et table
                          SavingAccount

    3 - Joined Table : hada tay3ni ghadi ndiro 3 tables ay table pour BankAccount et table pour CurrentAccount et table
                       pour SavingAccount w had les 2 tables enfants ghaykon fihom un clé étranger dial BankAccount

Donc hna ghadi njarbohom b 3 : bach ndiro had statégie khass ndiro l'annotation @Inheritance() w tema tan7adedo strategy

- ila drna single Table darori khass na3tiw m3aha wahd @DiscriminatorColumn rah char7 lfo9 achno taydir w les classes
enfants khass na3tiwhom @DiscriminatorValue.

- ila drna Table per class ghadi ghir n7adedo la strategy f @Inheritance() w ghadi nzido l class parent abstract bach
may créyich lina un table b smito hit hna khasso ycréé ghir les tables enfants

- ila drna Joined ghadi ghir n7adedo la strategy f @Inheritance() w safi
*/