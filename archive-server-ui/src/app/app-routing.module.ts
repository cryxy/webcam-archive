import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StartPageComponent } from './start-page/start-page.component';
import { WebcamDetailsComponent } from './webcam-details/webcam-details.component';
import { EventDetailsComponent } from './events/event-details/event-details.component';

const routes: Routes = [
  { path: 'start', component: StartPageComponent },
  { path: 'webcam/:id/events', component: WebcamDetailsComponent, data: { events: true } },
  { path: 'webcam/:id/settings', component: WebcamDetailsComponent, data: { events: false } },
  {path: 'webcam/:id/events/:eventId',component: EventDetailsComponent},
  {
    path: '',
    redirectTo: '/start',
    pathMatch: 'full'
  },

];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule { }
