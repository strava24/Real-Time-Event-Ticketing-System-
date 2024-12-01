import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment.development';
import { Constant } from '../../constant/Constant';

@Injectable({
  providedIn: 'root'
})
export class TicketPoolService {

  constructor(private http: HttpClient) { }

  getAllPoolsByEvent(id: number) {
    return this.http.get(environment.API_URL + Constant.TICKETPOOL_METHOD.GET_ALL_TICKETPOOLS(id));
  }

  sellTicket(poolID: number) { // should get the vendorID as parameter as well
    return this.http.get(environment.API_URL + Constant.TICKETPOOL_METHOD.SELL_TICKET(poolID, 1), { responseType: 'text' }); // hadrcoding vendor ID for now
  }


}
