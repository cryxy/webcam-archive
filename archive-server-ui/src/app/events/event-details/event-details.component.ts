import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { EventDto } from '../../api/generated/model';
import { EventService } from '../../services/event.service';
import { ActivatedRoute } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {

  event: EventDto;
  apiHost: string = environment.apiHost;

  constructor(private eventService:EventService, private activatedRoute:ActivatedRoute, private location: Location) { }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      console.log('[EventDetailsComponent] Changed event: ', params.eventId)
      this.eventService.getEvent(params.eventId).subscribe(event => {this.event = event});
    });
  }

  goBack() {
    this.location.back();
  }

  getSnapshotUrl(id: number): string {
    return this.apiHost + "/varchive/api/snapshots/" + id + "/image.jpg?size=MEDIUM";

  }

}
