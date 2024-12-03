import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Constant } from '../../constant/Constant';
import { Observable } from 'rxjs';
import { Events } from '../../model/class/Event';
import { LoginService } from '../loginService/login.service';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient, private loginService: LoginService) { }

  getAllEvents() {
    return this.http.get(environment.API_URL + Constant.EVENT_METHOD.GET_ALL_EVENTS);
  }

  getAllEventsByVendor(vendorID: number) {

    return this.http.get(environment.API_URL + Constant.EVENT_METHOD.GET_EVENTS_BY_VENDOR(vendorID));

  }

  updateEvent(event: Events) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(environment.API_URL + Constant.EVENT_METHOD.UPDATE_EVENT, event, { headers, responseType: 'text' });
  }

  deleteEvent(id: number) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.delete(environment.API_URL + Constant.EVENT_METHOD.DELETE_EVENT(id), { headers, responseType: 'text' });
  }

  saveEvent(event: Events) {
    return this.http.post(environment.API_URL + Constant.EVENT_METHOD.ADD_EVENT, event);
  }

}
