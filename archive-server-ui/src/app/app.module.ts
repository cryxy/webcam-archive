import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { LazyLoadImageModule } from 'ng-lazyload-image';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MainNavComponent } from './main-nav/main-nav.component';
import { LayoutModule } from '@angular/cdk/layout';
import { AppRoutingModule } from './/app-routing.module';
import { StartPageComponent } from './start-page/start-page.component';
import { WebcamCardComponent } from './webcam-card/webcam-card.component';
import { WebcamDetailsComponent } from './webcam-details/webcam-details.component';
import { WebcamSettingsComponent } from './webcam-settings/webcam-settings.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { EventOverviewComponent } from './events/event-overview/event-overview.component';

import { LOCALE_ID, NgModule } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
import { EventDetailsComponent } from './events/event-details/event-details.component';

@NgModule({
  declarations: [
    AppComponent,
    MainNavComponent,
    StartPageComponent,
    WebcamCardComponent,
    WebcamDetailsComponent,
    WebcamSettingsComponent,
    EventOverviewComponent,
    EventDetailsComponent
  ],
  imports: [
    BrowserModule, BrowserAnimationsModule, MatButtonModule, LayoutModule, MatToolbarModule, MatSidenavModule, MatIconModule, MatListModule, MatCardModule, FlexLayoutModule, MatTabsModule, MatFormFieldModule, MatOptionModule, MatSelectModule, MatInputModule, HttpClientModule, AppRoutingModule, MatSnackBarModule, MatProgressSpinnerModule, MatBadgeModule, LazyLoadImageModule ,FormsModule
  ],
  providers: [{ provide: LOCALE_ID, useValue: 'de' }],
  bootstrap: [AppComponent]
})
export class AppModule { }

// the second parameter 'fr' is optional
registerLocaleData(localeDe, 'de');
