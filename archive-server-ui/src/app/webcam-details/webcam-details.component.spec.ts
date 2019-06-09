import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WebcamDetailsComponent } from './webcam-details.component';

describe('WebcamDetailsComponent', () => {
  let component: WebcamDetailsComponent;
  let fixture: ComponentFixture<WebcamDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WebcamDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WebcamDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
