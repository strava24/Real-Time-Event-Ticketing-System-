import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Constant } from '../../constant/Constant';
import { Observable } from 'rxjs';
import { TicketPool } from '../../model/class/TicketPool';

@Injectable({
  providedIn: 'root'
})
export class TicketPoolService {

  constructor(private http: HttpClient) { }

  getAllPoolsByEvent(id: number) {
    return this.http.get(environment.API_URL + Constant.TICKETPOOL_METHOD.GET_ALL_TICKETPOOLS(id));
  }

  sellTicket(poolID: number, vendorID: number) {
    return this.http.get(environment.API_URL + Constant.TICKETPOOL_METHOD.SELL_TICKET(poolID, vendorID), { responseType: 'text' });
  }

  buyTicket(poolID: number, customerID: number) {
    return this.http.get(environment.API_URL + Constant.TICKETPOOL_METHOD.BUY_TICKET(poolID, customerID), { responseType: 'text' })
  }

  createTicketPool(
    eventID: number,
    ticketPool: TicketPool
  ): Observable<{ [key: string]: number }> {

    // Set up request parameters
    let params = new HttpParams()
      .set('poolName', ticketPool.poolName)
      .set('ticketPrice', ticketPool.ticketPrice.toString())
      .set('maxTicketCapacity', ticketPool.maxTicketCapacity.toString())
      .set('totalTickets', ticketPool.totalTickets.toString());

    return this.http.post<{ [key: string]: number }>(
      environment.API_URL + Constant.TICKETPOOL_METHOD.CREATE_POOL(eventID),
      {}, // Empty body
      { params } // Pass the parameters here
    );

  }

}
