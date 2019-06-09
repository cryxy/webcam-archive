import { Component, OnInit } from '@angular/core';
import { WebcamService } from '../services/webcam.service';
import { WebcamDto } from '../api/generated/model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-webcam-details',
  templateUrl: './webcam-details.component.html',
  styleUrls: ['./webcam-details.component.css']
})
export class WebcamDetailsComponent implements OnInit {

  webcam: WebcamDto;
  events: boolean = true;

  constructor(private route: ActivatedRoute,
    private webcamService: WebcamService) { }

  ngOnInit() {
    //const id = this.route.snapshot.paramMap.get('id');
    this.route.params.subscribe(params => {
      console.log('[WebcamDetailsComponente] Changed webcam: ', params.id)
      this.webcamService.getWebcam(params.id).subscribe(webcam => this.webcam = webcam);
    });

    this.route.data.subscribe(data => {
      this.events = data.events;
    })
    
  }



}
