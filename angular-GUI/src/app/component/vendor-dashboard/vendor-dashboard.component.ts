import { Component, inject, OnInit } from '@angular/core';
import { EventService } from '../../service/eventService/event.service';
import { Events } from '../../model/class/Event';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketPoolService } from '../../service/ticketPoolService/ticket-pool.service';
import { TicketPool } from '../../model/class/TicketPool';
import { ToastrService } from 'ngx-toastr';

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

  minimumDate: string; // Format: YYYY-MM-DD
  // vendorID: number = 1; // For now hard coded value

  eventService = inject(EventService);
  ticketPoolService = inject(TicketPoolService);
  toastrService = inject(ToastrService);

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
    this.eventService.getAllEventsByVendor().subscribe((getResponse: any) => {
      this.eventList = getResponse;
      console.log(this.eventList);
      this.toastrService.success('Loaded sucessfully!')
    }, error => {
      alert('Network Down!')
      this.toastrService.error('Network Down!')
    })
  }

  /**
   * Method to update an event on the system
   */
  onUpdate() {
    this.eventService.updateEvent(this.eventObj).subscribe({
      next: (response) => {
        console.log('Update Success:', response);
        this.toastrService.success('Updated Successfully!')
        this.eventObj = new Events();
        this.getAllEvents();
      },
      error: (error) => {
        this.toastrService.error('There was an issue on the process!');
        if (error.status === 404) {
          console.error('Event not found');
          this.toastrService.info('Event not found');
        } else {
          console.error('Update failed:', error);
        }
      },
    });
  }

  /**
   * Method to create a new event on the system
   */
  onSave() {
    this.eventService.saveEvent(this.eventObj).subscribe((response: any) => {
      this.toastrService.success('Event saved successfully!', 'Success!');
      this.eventObj = new Events();
      this.getAllEvents();
    }, error => {
      this.toastrService.error('There was an issue');
    })
  }

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

  accessPool(eventID: number) {
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

  stopTicketSelling(): void {
    if (this.isSelling) {
      this.toastrService.info('Stopping Ticket Production!')
      clearInterval(this.intervalId);
      this.isSelling = false;
    }
  }

  startTicketSelling(id: number,): void {

    if (!this.isSelling) {

      this.isSelling = true;
      this.intervalId = setInterval(() => {
        this.ticketPoolService.sellTicket(id).subscribe({
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
    }
  }

  ngOnDestroy(): void {
    this.stopTicketSelling(); // Cleanup logic
    console.log('VendorComponent destroyed');
  }

  savePool() {

    console.log('Hello')
    this.ticketPoolService.createTicketPool(this.currentEvent, this.ticketPoolObj).subscribe(
      (response) => {
        this.toastrService.success('Created Ticket Pool!');
        this.ticketPoolObj = new TicketPool();
      },
      error => {
        console.log('There was an issue')
      }
    )

  }

}
