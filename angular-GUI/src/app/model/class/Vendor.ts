export class Vendor {

    vendorID: number
    vendorName: string
    vendorEmail: string
    vendorPassword: string
    ticketsSold: number

    constructor() {
        this.vendorID = 0;
        this.vendorName = '';
        this.vendorEmail = '';
        this.vendorPassword = '';
        this.ticketsSold = 0;
    }
}