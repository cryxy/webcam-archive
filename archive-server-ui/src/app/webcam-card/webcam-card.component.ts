import { Component, OnInit, Input } from '@angular/core';
import {WebcamDto} from '../api/generated/defs/WebcamDto'
import { environment } from '../../environments/environment';

@Component({
  selector: 'webcam-card',
  templateUrl: './webcam-card.component.html',
  styleUrls: ['./webcam-card.component.css']
})
export class WebcamCardComponent implements OnInit {

  @Input() webcam: WebcamDto;

  apiHost: string = environment.apiHost;

  constructor() { }

  openFullScreen() {
    window.open(this.apiHost + "/varchive/api/webcams/"+ this.webcam.id + "/snapshots/latest/image.jpg?size=ORIGINAL"); 
  }
  
  ngOnInit() {
  }

}
