import { Component, inject, OnInit } from '@angular/core';
import { EventService } from '../../service/event.service';
import { Events } from '../../model/class/Event';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

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
  minimumDate: string; // Format: YYYY-MM-DD
  // vendorID: number = 1; // For now hard coded value

  eventService = inject(EventService);

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
    }, error => {
      alert('Network Down!')
    })
  }

  /**
   * Method to update an event on the system
   */
  onUpdate() {
    this.eventService.updateEvent(this.eventObj).subscribe({
      next: (response) => {
        alert('Updated Successfully!')
        console.log('Update Success:', response);
        this.eventObj = new Events();
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

  /**
   * Method to create a new event on the system
   */
  onSave() {
    this.eventService.saveEvent(this.eventObj).subscribe((response: any) => {
      alert('Saved Event!');
      this.eventObj = new Events();
      this.getAllEvents();
    }, error => {
      alert('There was an error')
    })
  }

  onDelete(id: number) {

    const confirmation = confirm('Would you like to permenantly delete this event?');

    if (confirmation) {
      this.eventService.deleteEvent(id).subscribe({
        next: (response) => {
          alert('Deleted Successfully!')
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






}
