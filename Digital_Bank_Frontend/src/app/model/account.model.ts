import { Customer } from "./customer.model";

export interface AccountDetails {
    accountId:            string;
    balance:              number;
    accountOperationDTOS : AccountOperation[];
    currentPage:          number;
    totalPages:           number;
    pageSize:             number;
}

export interface AccountOperation {
    id:            number;
    operationDate: Date;
    amount:        number;
    type:          string;
    description:   string;
}

export interface AccountCustomer {
    type:          string;
    id:            string;
    balance:       number;
    createdAt:     Date;
    status:        null;
    customerDTO:   Customer;
    overDraft?:    number;
    interestRate?: number;
}

/* had les 2 interfaces générinahom b had site https://app.quicktype.io/?l=ts w li tay transformer 
les données Json en Model en TypeScript 

hna 3tinah had résultat Json li lta7et w 7awlhom lina l des interfaces
{
  "accountId": "0731620f-ea35-4a1a-9793-aaa668b285ef",
  "balance": 14931.6655906846,
  "accountOperationDTOS": [
    {
      "id": 1,
      "operationDate": "2024-04-06T15:20:02.000+00:00",
      "amount": 11431.9672284159,
      "type": "CREDIT",
      "description": "Credit"
    },
    {
      "id": 2,
      "operationDate": "2024-04-06T15:20:02.000+00:00",
      "amount": 7456.00985683817,
      "type": "DEBIT",
      "description": "Debit"
    }
  ],
  "currentPage": 0,
  "totalPages": 2,
  "pageSize": 2
}
*/
