import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {WebcamDto} from '../api/generated/defs/WebcamDto';
import {environment} from '../../environments/environment';
import {WEBCAMS} from '../mock-webcams';

@Injectable({
  providedIn: 'root'
})
export class WebcamService {

  private basePath: string =  '/varchive/api/webcams';
  private resourceUrl: string = environment.apiHost + this.basePath;

  constructor(private httpClient: HttpClient) { }

  getWebcams(): Observable<WebcamDto[]> {
    return this.httpClient.get<WebcamDto[]>(this.resourceUrl);
    //return of(WEBCAMS);
  }

  getWebcam(id: string): Observable<WebcamDto> {
    return this.httpClient.get<WebcamDto>(this.resourceUrl+'/'+id);
  }

  updateWebcam(webcam: WebcamDto): Observable<{}> {
    return this.httpClient.put<{}>(this.resourceUrl+'/'+webcam.id,webcam);
  }


}
