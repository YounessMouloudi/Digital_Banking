package com.projects.digital_bank_backend.dtos;

import lombok.Data;

import java.util.List;

/* On a fait cette class juste pour afficher la listes des operations */
@Data
public class AccountHistoryDTO {

    private String accountId;
    private Double balance;
    private List<AccountOperationDTO> accountOperationDTOS;

    // ça c'est pour la pagination
    private int currentPage;
    private int totalPages;
    private int pageSize;
}
