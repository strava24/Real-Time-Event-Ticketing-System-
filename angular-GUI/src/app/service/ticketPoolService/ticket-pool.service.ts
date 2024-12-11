import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Constant } from '../../constant/Constant';
import { Observable, Subscription, switchMap, timer } from 'rxjs';
import { TicketPool } from '../../model/class/TicketPool';

@Injectable({
  providedIn: 'root'
})
export class TicketPoolService {

  private pollingSubscription: Subscription | null = null;

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

  /**
 * Fetch ticket pool details with short polling.
 * Polls every 5 seconds (adjust as needed).
 */
  getTicketPoolDetails(id: number): Observable<any> {
    return timer(0, 5000).pipe(
      switchMap(() => this.http.get(environment.API_URL + Constant.TICKETPOOL_METHOD.GET_ALL_TICKETPOOLS(id)))
    );
  }

  /**
   * Starts polling ticket pool details for a specific event every 5 seconds.
   */
  startPolling(eventId: number, callback: (data: any) => void): void {
    this.stopPolling();
    this.pollingSubscription = timer(0, 5000).pipe(
      switchMap(() => this.http.get<any>(environment.API_URL + Constant.TICKETPOOL_METHOD.GET_ALL_TICKETPOOLS(eventId)))
    ).subscribe(callback);
  }

  /**
   * Stops polling ticket pool details.
   */
  stopPolling(): void {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
      this.pollingSubscription = null;
    }
  }



}
