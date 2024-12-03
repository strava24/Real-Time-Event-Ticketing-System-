export class Events {
    eventID: number;
    eventName: string;
    vendorID: number;
    date: Date;
    location: string;

    constructor(eventID: number = 0, eventName: string = '', vendorID: number = 0, date: string = '', location: string = '') {
        this.eventID = eventID;
        this.eventName = eventName;
        this.vendorID = vendorID;
        this.date = new Date(date); // Convert string to Date object
        this.location = location;
    }
}