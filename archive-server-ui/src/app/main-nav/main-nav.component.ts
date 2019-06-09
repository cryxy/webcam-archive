import { Component, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { WebcamService } from '../services/webcam.service';
import { WebcamDto } from '../api/generated/defs/WebcamDto';

@Component({
  selector: 'main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css']
})
export class MainNavComponent implements OnInit {

  webcams: WebcamDto[];
  title = 'Webcam Archive Server';

  ngOnInit(): void {
    this.webcamService.getWebcams().subscribe(webcams => this.webcams = webcams);
  }

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  constructor(private breakpointObserver: BreakpointObserver, private webcamService: WebcamService) { }

}
