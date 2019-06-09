import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WebcamCardComponent } from './webcam-card.component';
import { MatCardModule } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';

import {WEBCAMS} from '../mock-webcams';

describe('WebcamCardComponent', () => {
  let component: WebcamCardComponent;
  let fixture: ComponentFixture<WebcamCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WebcamCardComponent ],
      imports: [MatCardModule,RouterTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WebcamCardComponent);
    component = fixture.componentInstance;
    component.webcam = WEBCAMS[0];
    fixture.detectChanges();
  });

  it('should create', () => {

    expect(component).toBeTruthy();
  });
});
