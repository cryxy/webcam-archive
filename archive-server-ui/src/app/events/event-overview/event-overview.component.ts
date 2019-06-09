import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { WebcamDto, EventDto } from '../../api/generated/model';
import { EventService } from '../../services/event.service';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'event-overview',
  templateUrl: './event-overview.component.html',
  styleUrls: ['./event-overview.component.css']
})
export class EventOverviewComponent implements OnInit, OnChanges {

  @Input() webcam: WebcamDto;

  apiHost: string = environment.apiHost;

  eventDates: Date[];
  selectedStartDate: string;
  loadingEvents: boolean = false;
  events: EventDto[];

  constructor(private eventService: EventService, private router: Router, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    console.log('[EventOverviewComponent] Initialize component for webcam=', this.webcam);
    this.loadEventDatesAndUpdateRoute();
    this.activatedRoute.queryParams.subscribe(params => {
      if(params.minSnapshotsThreshold !== undefined) {
        this.webcam.minSnapshotsThreshold = params.minSnapshotsThreshold;
      }
      if(params.date !== undefined) {
        this.selectedStartDate = params.date;
        this.loadEvents();
      }
      
    });
  }

  /** The webcam has changed */
  ngOnChanges() {
    this.selectedStartDate = null;  
    this.loadEventDatesAndUpdateRoute();
  }

  onChangedValue() {
    this.updateRoute();
  }

  openEvent(eventId: number) {
    console.log('[EventOverviewComponent] openEvent:', eventId)
    // /webcam/{{webcam.id}}/events/{{event.id}}
    this.router.navigate([eventId], { relativeTo: this.activatedRoute });

  }

  private loadEventDatesAndUpdateRoute(): void {
    this.events = null;
    this.eventService.getEventDates(this.webcam.id).subscribe(dates => {
      this.eventDates = dates
      if (this.eventDates.length > 0 && this.selectedStartDate === null) {
        this.selectedStartDate = this.eventDates[0].toString();
      }
      this.updateRoute();
    });
  };

  private loadEvents(): void {
    if(this.selectedStartDate === null) {
      console.log('[EventOverviewComponent] No selectedStartDate. Loading events not possible!');
    }
    this.loadingEvents = true;
    this.eventService.getEvents(this.webcam.id, new Date(this.selectedStartDate), this.webcam.minSnapshotsThreshold).subscribe(events => {
      this.loadingEvents = false;
      this.events = events;
    });
  }

  private updateRoute(): void {
    console.log('[EventOverviewComponent] Setting navigation link!');
    this.router.navigate([], { relativeTo: this.activatedRoute, queryParams: { date: this.selectedStartDate, minSnapshotsThreshold: this.webcam.minSnapshotsThreshold } }).catch(error => console.log('[EventOverviewComponent] Error navigating!', error));
  }

  getSnapshotUrl(id: number): string {
    // src="{{apiHost}}/varchive/api/snapshots/{{event.snapshots[0].id}}/image.jpg?size=MEDIUM"
    return this.apiHost + "/varchive/api/snapshots/" + id + "/image.jpg?size=MEDIUM";
  }

}
