import { Component, OnInit, Input } from '@angular/core';
import { WebcamDto } from '../api/generated/model';
import { WebcamService } from '../services/webcam.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'webcam-settings',
  templateUrl: './webcam-settings.component.html',
  styleUrls: ['./webcam-settings.component.css']
})
export class WebcamSettingsComponent implements OnInit {

  @Input() webcam: WebcamDto;

  constructor(private webcamService: WebcamService, public snackBar: MatSnackBar) { }

  ngOnInit() {
  }

  onSubmit() {
    this.webcamService.updateWebcam(this.webcam).subscribe(result => { 
      
      console.log("erfolgreich")});
      this.snackBar.open("Update erfolgreich.",'OK', {
        duration: 3000
      }), 
      error => {
        this.snackBar.open("Update fehlgeschlagen.")
      }
  }

}
