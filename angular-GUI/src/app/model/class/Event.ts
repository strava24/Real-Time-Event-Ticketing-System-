export class Events {
    eventID: number;
    eventName: string;
    vendorID: number;
    date: Date;
    location: string;
    imageName: string;
    imageUrl: string; // Optional field to store the image URL once it's fetched

    constructor(
        eventID: number = 0,
        eventName: string = '',
        vendorID: number = 0,
        date: string = '',
        location: string = '',
        imageName: string = '',
        imageUrl: string = ''
    ) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.vendorID = vendorID;
        this.date = new Date(date); // Convert string to Date object
        this.location = location;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }
}