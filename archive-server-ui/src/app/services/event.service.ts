import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { EventDto } from '../api/generated/defs/EventDto';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class EventService {

  private basePath: string =  '/varchive/api/events';
  private resourceUrl: string = environment.apiHost + this.basePath;

  constructor(private httpClient: HttpClient) { }

  getEventDates(webcamId: number): Observable<Date[]> {

    console.log('[EventService] getEventDates for ', webcamId);
    // Add safe, URL encoded search parameter if there is a search term
    const options = { params: new HttpParams().set('webcamId', webcamId.toString()) } ;

    return this.httpClient.get<Date[]>(`${this.resourceUrl}/dates`, options);

  }

  getEvents(webcamId: number, date: Date, minSnapshots: number): Observable<EventDto[]> {
    console.log('[EventService] getEvents for ', webcamId,date);

    let beginDate: Date = new Date(date);
    beginDate.setHours(0);
    beginDate.setMinutes(0);
    beginDate.setSeconds(0);
   
    let endDate: Date = new Date(date);
    endDate.setHours(23);
    endDate.setMinutes(59);
    endDate.setSeconds(59);

    let queryParams = {webcamId : webcamId.toString(),beginDate : beginDate.toISOString(), endDate : endDate.toISOString(),minSnapshots : minSnapshots.toString(),withSnapshotsPreview:"true"};
    console.log('[EventService] Query params: ', queryParams);

     return this.httpClient.get<EventDto[]>(`${this.resourceUrl}`, {params: queryParams});

  } 

  getEvent(id: number): Observable<EventDto> {
    return this.httpClient.get<EventDto>(`${this.resourceUrl}/${id}`);
  }



}
