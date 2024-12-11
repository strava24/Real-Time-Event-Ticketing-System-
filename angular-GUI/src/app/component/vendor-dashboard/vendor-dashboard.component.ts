import { Component, inject, OnInit } from '@angular/core';
import { EventService } from '../../service/eventService/event.service';
import { Events } from '../../model/class/Event';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketPoolService } from '../../service/ticketPoolService/ticket-pool.service';
import { TicketPool } from '../../model/class/TicketPool';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from '../../service/loginService/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-vendor-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vendor-dashboard.component.html',
  styleUrl: './vendor-dashboard.component.css'
})

export class VendorDashboardComponent implements OnInit {

  eventList: Events[] = []; // List to hold the events on the database
  eventObj: Events = new Events(); // Events object to hold the event details on the form

  ticketPoolList: TicketPool[] = []; // List to hold the ticket pools of an event
  hasPools: boolean = false;
  currentEventID: number = 0; // ID of the currently selected event
  currentEventName: string = '';
  ticketPoolObj: TicketPool = new TicketPool(); // TicketPool obj to hold the Ticket pool details on thr form

  private intervalId: any;
  isSelling: boolean = false;

  ticketRetrievalRate: number = 0;
  ticketReleaseRate: number = 1000; // ticketReleaseRate set to 1 second by default

  notificationList: String[] = [];

  minimumDate: string; // Format: YYYY-MM-DD

  selectedFile: File | null = null;

  eventService = inject(EventService);
  ticketPoolService = inject(TicketPoolService);
  toastrService = inject(ToastrService);
  loginService = inject(LoginService);
  router = inject(Router);

  constructor() {
    this.minimumDate = new Date().toISOString().split('T')[0];
  }

  ngOnInit(): void {
    this.getAllEvents(); // Loading all the events at the start of the program

  }

  /**
   * Method to get all of the events by the current vendor
   */
  getAllEvents() {

    const vendorID = this.loginService.getVendorID();

    if (vendorID != -1) {
      this.eventService.getAllEventsByVendor(vendorID).subscribe((getResponse: any) => {
        this.toastrService.info('Logged in with ID : V' + vendorID);
        this.eventList = getResponse;
        console.log(this.eventList);
        this.toastrService.success('Loaded sucessfully!')
      }, error => {
        if (error.status === 501) {
          this.toastrService.error('There are no Events yet!');
        } else {
          this.router.navigateByUrl('/login');
          this.toastrService.error('Login to the system!', 'Invalid Credentials');
        }

      })

    } else {
      this.toastrService.error('Login to the system!');
      this.router.navigateByUrl('/login');
    }

  }

  /**
   * Method to update event details
   */
  onUpdate() {
    this.eventObj.vendorID = this.loginService.getVendorID();

    console.log('eventID : ' + this.eventObj.eventID);

    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('eventDTO', new Blob([JSON.stringify(this.eventObj)], { type: 'application/json' }));
      formData.append('imageFile', this.selectedFile);

      this.eventService.updateEventWithImage(formData)
        .subscribe(
          response => {
            console.log('Event created successfully:', response);
            this.toastrService.success('Created event successfully!');
            this.getAllEvents();
            this.eventObj = new Events();
          },
          error => {
            console.error('Error creating event:', error);
          }
        );
    } else {
      this.toastrService.info("You have to upload the image!")
    }


  }

  /**
   * Method to save event details
   */
  onSave() {

    this.eventObj.vendorID = this.loginService.getVendorID();

    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('eventDTO', new Blob([JSON.stringify(this.eventObj)], { type: 'application/json' }));
      formData.append('imageFile', this.selectedFile);

      this.eventService.saveEventWithImage(formData)
        .subscribe(
          response => {
            console.log('Event created successfully:', response);
            this.toastrService.success('Created event successfully!');
            this.getAllEvents();
            this.eventObj = new Events();
          },
          error => {
            console.error('Error creating event:', error);
          }
        );
    }

  }

  /**
   * Method to delete event details
   * @param id - Event ID
   */
  onDelete(id: number) {

    const confirmation = confirm('Would you like to permenantly delete this event?');

    if (confirmation) {
      this.eventService.deleteEvent(id).subscribe({
        next: (response) => {
          this.toastrService.success('Deleted Successfully!')
          console.log('Delete Success:', response);
          this.getAllEvents();
        },
        error: (error) => {
          alert('There was an issue on the process!')
          if (error.status === 404) {
            console.error('Event not found');
          } else {
            console.error('Update failed:', error);
          }
        },
      });
    }

  }

  onEdit(obj: Events) {
    this.eventObj = obj;
  }

  /**
   * Method to access pools of an event
   * @param eventID - Event ID
   */
  accessPool(eventID: number, eventName: string) {

    this.stopPolling(); // Stopping the on goin polling if is it happening
    this.startPolling(eventID); // start polling for current event
    // this.getRealTimePoolUpdates(eventID); // Starting the short polling process

    this.ticketPoolService.getAllPoolsByEvent(eventID).subscribe(
      (getResponse: any) => {
        if (getResponse.length === 0) {
          console.log('There are no pools for this event yet!');
          this.toastrService.info('There are no pools for this event!');
          this.hasPools = false;
        } else {
          this.ticketPoolList = getResponse;
          this.hasPools = true;
          this.currentEventID = eventID;
          this.currentEventName = eventName;
          console.log(this.ticketPoolList);
          this.toastrService.success('Loaded Ticket Pools!', 'Success!!');
        }
      },
      error => {
        if (error.status === 404) {
          console.log('There are no pools for this event yet!');
          this.toastrService.error('There are no pools for this event yet!');
          this.hasPools = false;
          this.currentEventID = eventID;
          this.currentEventName = eventName;
        } else {
          this.toastrService.error('Network Down!');
        }
      }
    );
  }

  /**
   * Tis method is to make sure that only 20 notifications are listed at a time to maintain a good UI
   * @param newNotifications 
   */
  addNotifications(newNotifications: string[]) {
    this.notificationList.push(...newNotifications);

    // To nsure the array size doesn't exceed 20
    if (this.notificationList.length > 20) {
      this.notificationList.splice(0, this.notificationList.length - 20); // Removing excess notifications
    }
  }

  /**
   * Method to stop ticket selling
   */
  stopTicketSelling(): void {
    if (this.isSelling) {
      this.toastrService.info('Stopping Ticket Production!')
      clearInterval(this.intervalId);
      this.isSelling = false;
    }
  }

  /**
   * Method to start ticket selling
   * @param poolIDd + pool ID
   */
  startTicketSelling(poolIDd: number): void {

    this.openInput();

    const vendorID: number = this.loginService.getVendorID();

    if (!this.isSelling && vendorID !== -1) {

      this.isSelling = true;
      this.intervalId = setInterval(() => {
        this.ticketPoolService.sellTicket(poolIDd, vendorID).subscribe({
          next: (response) => {
            this.toastrService.info('Ticket Sold!');
            console.log('Ticket sold:', response);
            this.accessPool(this.currentEventID, this.currentEventName);
          },
          error: (error) => {
            if (error.status === 400) {
              this.toastrService.info('The ticket pool is full! Cannot add more tickets!');
              this.stopTicketSelling();
              console.warn('Failed to sell ticket:', error.error); // Log the message without crashing
            } else {
              console.error('Unexpected error:', error);
            }
          }
        });
      }, this.ticketReleaseRate);
    } else {
      this.toastrService.error('Login to the system!')
    }
  }


  ngOnDestroy(): void {
    this.stopTicketSelling(); // Cleanup logic
    console.log('VendorComponent destroyed');
  }

  /**
   * Method to save a ticket pool
   */
  savePool() {

    this.ticketPoolService.createTicketPool(this.currentEventID, this.ticketPoolObj).subscribe(
      (response) => {
        this.toastrService.success('Created Ticket Pool!');
        this.ticketPoolObj = new TicketPool();
        this.accessPool(this.currentEventID, this.currentEventName);
      },
      error => {
        console.log('There was an issue')
      }
    )

  }

  /**
   * Method to be called when the user uploads an image
   * @param event - event of uploading a file
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  /**
   * Method to start polling to fetch for latest ticket details
   */
  startPolling(eventID: number): void {

    this.ticketPoolService.startPolling(eventID, (response) => {
      const poolList: TicketPool[] = response;
      const formatedPoolList: string[] = [];

      for (let i = 0; i < poolList.length; i++) {
        const ticketsBought = poolList[i].ticketsBought;
        const ticketsSold = poolList[i].ticketsSold;
        const name = poolList[i].poolName;
        const id = poolList[i].poolID;

        // Converting the infprmation into a notification and pushing to the notification list
        formatedPoolList.push(ticketsBought + " tickets bought out of " + ticketsSold + " produced tickets : " + name + " tickets (P" + id + ") for event " + this.currentEventName + " (E" + this.currentEventID + ")");
      }

      this.addNotifications(formatedPoolList);
    });
  }

  stopPolling(): void {
    this.ticketPoolService.stopPolling();
  }

  /**
   * Method to get valid ticketReleaseRate from the user
   */
  openInput() {
    // Keep prompting until the input is valid
    while (true) {
      const input = prompt("Enter ticket realease rate (in seconds) : ");

      // If user cancels (input is null), exit the loop
      if (input === null) {
        this.toastrService.info('Release rate is set to 1 second!')
        break;
      }

      // Convert input to a number
      this.ticketReleaseRate = parseFloat(input);

      // Check if the input is a valid number and greater than 0
      if (!isNaN(this.ticketReleaseRate) && this.ticketReleaseRate > 0) {
        this.toastrService.info(`Release rate is set to: ${this.ticketReleaseRate} seconds`);
        this.ticketReleaseRate = this.ticketReleaseRate * 1000;
        break; // Exit the loop if input is valid
      } else {
        this.toastrService.error("Please enter a valid number greater than 0.");
      }
    }
  }

}