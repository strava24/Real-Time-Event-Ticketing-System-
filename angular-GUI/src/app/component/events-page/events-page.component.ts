import { AfterViewInit, Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { EventService } from '../../service/eventService/event.service';
import { Events } from '../../model/class/Event';
import { ToastrService } from 'ngx-toastr';
import { CommonModule, DatePipe } from '@angular/common';
import { TicketPoolService } from '../../service/ticketPoolService/ticket-pool.service';
import { TicketPool } from '../../model/class/TicketPool';
import { LoginService } from '../../service/loginService/login.service';
import { HttpResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-events-page',
  standalone: true,
  imports: [DatePipe, CommonModule, FormsModule],
  templateUrl: './events-page.component.html',
  styleUrl: './events-page.component.css'
})

export class EventsPageComponent implements OnInit, AfterViewInit {

  eventService = inject(EventService);
  toastr = inject(ToastrService);
  ticketPoolService = inject(TicketPoolService);
  loginService = inject(LoginService);
  ticketPoolList: TicketPool[] = [];
  currentEvent: number = 0;
  currentVendorID: number = 0;
  currentVendor: string = '';
  toastrService = inject(ToastrService);
  ticketCount = 1;
  intervalId: any;
  ticketRetrievalRate = 1000; // Set to 1 second by default

  events: Events[] = [];
  hasPools: boolean = false;
  @ViewChild('offcanvasElement', { static: false }) offcanvasElement!: ElementRef;

  // eventImage: string | undefined;

  ngOnInit(): void {
    this.getAllEvents();
  }

  ngAfterViewInit() {
    const offcanvas = this.offcanvasElement.nativeElement;
    offcanvas.addEventListener('hide.bs.offcanvas', () => {
      this.onExit();
    });
  }

  /**
   * Method to get all the events on the system
   */
  getAllEvents() {
    this.eventService.getAllEvents().subscribe(
      (data: Events[]) => {
        console.log('TS', data);
        if (data && data.length > 0) {
          this.fetchImagesAndUpdateProducts(data);
        }
      },
      (error) => {
        console.error('Error fetching events', error);
      }
    );

  }

  /**
   * Method to fetch the images of the events in the back end
   * @param data - event data
   */
  async fetchImagesAndUpdateProducts(data: any[]) {
    const updatedEvents = await Promise.all(
      data.map(async (event) => {
        try {
          const imageBlob = await this.eventService.getImageByEventID(event.eventID).toPromise();

          // Check if the imageBlob is defined
          if (imageBlob) {
            const imageUrl = URL.createObjectURL(imageBlob);
            return { ...event, imageUrl };
          } else {
            console.error(`Image data for event ID ${event.eventID} is undefined.`);
            return { ...event, imageUrl: "images/defaultEvent.jpg" };
          }
        } catch (error) {
          console.error('Error fetching image for event ID:', event.eventID, error);
          return { ...event, imageUrl: "images/defaultEvent.jpg" };
        }
      })
    );
    console.log(updatedEvents)
    this.events = updatedEvents;
  }

  /**
   * Method to access pools of an event
   * @param eventID - Event ID of the event required
   * @param vendorID - vendor ID of the person hosting the event
   */
  accessPools(eventID: number, vendorID: number) {

    this.ticketPoolService.getAllPoolsByEvent(eventID).subscribe(
      (getResponse: any) => {
        if (getResponse.length === 0) {
          console.log('There are no pools for this event yet!');
          this.toastr.info('There are no pools for this event!');
        } else {
          this.hasPools = true;
          this.ticketPoolList = getResponse;
          this.currentEvent = eventID;
          console.log(this.ticketPoolList);
          this.toastr.success('Loaded Ticket Pools!', 'Success!!');
        }
      },
      error => {
        if (error.status === 404) {
          console.log('There are no pools for this event yet!');
          this.toastr.error('There are no pools for this event yet!');
          this.currentEvent = eventID;
        } else {
          this.toastr.error('Network Down!');
        }
      }
    );

    this.loginService.getVendorDetails(vendorID).subscribe((response) => {
      this.currentVendor = response.vendorName;
      this.currentVendorID = response.vendorID;
    }, error => {
      this.toastr.error("Couldn't load Vendor Details", "Network Down!");
    })

  }

  onExit() {
    this.hasPools = false;
  }

  /**
   * Method to buy tickets
   * @param poolID - pool ID from which the customer is trying to buy a ticket
   */
  onBuy(poolID: number) {

    if (this.ticketCount > 1) {
      this.openInput(); // Asking for retriival rate if count is grater than 0
    }

    const customerID: number = this.loginService.getCustomerID();

    if (customerID === -1) {
      this.toastrService.error('Login to the system!');
      return;
    }

    let count = 0;

    this.intervalId = setInterval(() => {
      if (count >= this.ticketCount) {
        clearInterval(this.intervalId); // Stop after requested tickets
        return;
      }

      this.ticketPoolService.buyTicket(poolID, customerID).subscribe({
        next: (response) => {
          this.toastrService.info('Ticket Bought!');
          console.log('Ticket bought:', response);
          this.accessPools(this.currentEvent, this.currentVendorID);
        },
        error: (error) => {
          if (error.status === 400) {
            this.toastrService.error('Tickets are sold out due to high demand');
            console.warn('Failed to buy ticket:', error.error);
            clearInterval(this.intervalId); // Stop on failure
          } else {
            console.error('Unexpected error:', error);
          }
        }
      });

      count++; // Increment after every successful interval
    }, this.ticketRetrievalRate);
  }

  /**
   * Method to get valid ticketRetrievalRate from the user
   */
  openInput() {
    // Keep prompting until the input is valid
    while (true) {
      const input = prompt("Enter ticket retrieval rate (in seconds) : ");

      // If user cancels (input is null), exit the loop
      if (input === null) {
        this.toastrService.info('Retrieval rate is set to 1 second!')
        break;
      }

      // Convert input to a number
      this.ticketRetrievalRate = parseFloat(input);

      // Check if the input is a valid number and greater than 0
      if (!isNaN(this.ticketRetrievalRate) && this.ticketRetrievalRate > 0) {
        this.toastrService.info(`Release rate is set to: ${this.ticketRetrievalRate} seconds`);
        this.ticketRetrievalRate = this.ticketRetrievalRate * 1000;
        break; // Exit the loop if input is valid
      } else {
        this.toastrService.error("Please enter a valid number greater than 0.");
      }
    }
  }


}