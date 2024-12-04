import { Component, inject, OnInit } from '@angular/core';
import { EventService } from '../../service/eventService/event.service';
import { Events } from '../../model/class/Event';
import { ToastrService } from 'ngx-toastr';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-events-page',
  standalone: true,
  imports: [DatePipe, CommonModule],
  templateUrl: './events-page.component.html',
  styleUrl: './events-page.component.css'
})

export class EventsPageComponent implements OnInit {

  eventService = inject(EventService);
  toastr = inject(ToastrService)

  events: Events[] = [];

  // eventImage: string | undefined;

  ngOnInit(): void {

    this.getAllEvents()

  }

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
        // Handle error case here if needed
      }
    );

  }

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

  onClick() {
    console.log('Loading pools')
    this.toastr.info('Loading available pools!')
  }


}



