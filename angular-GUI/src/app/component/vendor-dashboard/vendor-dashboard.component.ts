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

  eventList: Events[] = [];
  eventObj: Events = new Events();

  ticketPoolList: TicketPool[] = [];
  hasPools: boolean = false;
  currentEvent: number = 0;
  ticketPoolObj: TicketPool = new TicketPool();

  private intervalId: any;
  isSelling: boolean = false;

  ticketRetrievalRate: number = 0;

  notificationList: String[] = [];

  minimumDate: string; // Format: YYYY-MM-DD
  // vendorID: number = 1; // For now hard coded value

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
    this.getAllEvents();

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
   * Method to update an event on the system
   */
  // onUpdate() {
  //   this.eventService.updateEvent(this.eventObj).subscribe({
  //     next: (response) => {
  //       console.log('Update Success:', response);
  //       this.toastrService.success('Updated Successfully!')
  //       this.eventObj = new Events();
  //       this.getAllEvents();
  //     },
  //     error: (error) => {
  //       this.toastrService.error('There was an issue on the process!');
  //       if (error.status === 404) {
  //         console.error('Event not found');
  //         this.toastrService.info('Event not found');
  //       } else {
  //         console.error('Update failed:', error);
  //       }
  //     },
  //   });
  // }

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
  accessPool(eventID: number) {

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
          this.currentEvent = eventID;
          console.log(this.ticketPoolList);
          this.toastrService.success('Loaded Ticket Pools!', 'Success!!');
        }
      },
      error => {
        if (error.status === 404) {
          console.log('There are no pools for this event yet!');
          this.toastrService.error('There are no pools for this event yet!');
          this.hasPools = false;
          this.currentEvent = eventID;
        } else {
          this.toastrService.error('Network Down!');
        }
      }
    );
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

    const vendorID: number = this.loginService.getVendorID();

    if (!this.isSelling && vendorID !== -1) {

      this.isSelling = true;
      this.intervalId = setInterval(() => {
        this.ticketPoolService.sellTicket(poolIDd, vendorID).subscribe({
          next: (response) => {
            this.toastrService.info('Ticket Sold!');
            console.log('Ticket sold:', response);
            this.accessPool(this.currentEvent);
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
      }, 1000);
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

    console.log('Hello')
    this.ticketPoolService.createTicketPool(this.currentEvent, this.ticketPoolObj).subscribe(
      (response) => {
        this.toastrService.success('Created Ticket Pool!');
        this.ticketPoolObj = new TicketPool();
        this.accessPool(this.currentEvent);
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

  // getRealTimePoolUpdates(eventID: number) {

  //   this.notificationList = [];

  //   this.ticketPoolService.getTicketPoolDetails(eventID).subscribe(response => {
  //     const poolList: TicketPool[] = response;

  //     for (let i = 0; i < poolList.length; i++) {
  //       const ticketsBought = poolList[i].ticketsBought;
  //       const ticketsSold = poolList[i].ticketsSold;
  //       const name = poolList[i].poolName;
  //       const id = poolList[i].poolID;

  //       this.notificationList.push(ticketsBought + " tickets bought out of " + ticketsSold + " produced tickets on " + name + " : P" + id);
  //     }

  //     this.toastrService.info('New Notification!');

  //   }, error => {
  //     this.toastrService.error('There was an error while fetching the updates');
  //   });
  // }

  startPolling(eventID: number): void {

    this.ticketPoolService.startPolling(eventID, (response) => {
      const poolList: TicketPool[] = response;

      for (let i = 0; i < poolList.length; i++) {
        const ticketsBought = poolList[i].ticketsBought;
        const ticketsSold = poolList[i].ticketsSold;
        const name = poolList[i].poolName;
        const id = poolList[i].poolID;

        this.notificationList.push(ticketsBought + " tickets bought out of " + ticketsSold + " produced tickets on " + name + " : P" + id + " on event E" + this.currentEvent);
      }

      this.toastrService.info('New Notification!');
    });
  }

  stopPolling(): void {
    this.ticketPoolService.stopPolling();
  }

}
