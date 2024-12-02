export class TicketPool {
    poolID: number
    poolName: string
    maxTicketCapacity: number
    totalTickets: number
    ticketsSold: number
    ticketsBought: number
    ticketPrice: number

    constructor() {
        this.poolID = 0;
        this.poolName = '';
        this.maxTicketCapacity = 0;
        this.totalTickets = 0;
        this.ticketsSold = 0;
        this.ticketsBought = 0;
        this.ticketPrice = 0;
    }
}