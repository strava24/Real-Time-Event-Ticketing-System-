export class Customer {

    customerID: number;
    customerName: string;
    customerEmail: string;
    customerPassword: string;
    boughtTickets: number;

    constructor() {
        this.customerID = 0;
        this.customerName = '';
        this.customerEmail = '';
        this.customerPassword = '';
        this.boughtTickets = 0;
    }

}