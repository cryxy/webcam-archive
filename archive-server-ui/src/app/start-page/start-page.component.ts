import { Component, OnInit } from '@angular/core';
import { WebcamService } from '../services/webcam.service';
import { WebcamDto } from '../api/generated/model';

@Component({
  selector: 'app-start-page',
  templateUrl: './start-page.component.html',
  styleUrls: ['./start-page.component.css']
})
export class StartPageComponent implements OnInit {

  webcams: WebcamDto[];

  constructor(private webcamService: WebcamService) { }

  ngOnInit() {
    this.webcamService.getWebcams().subscribe(webcams => this.webcams = webcams);
  }

}
