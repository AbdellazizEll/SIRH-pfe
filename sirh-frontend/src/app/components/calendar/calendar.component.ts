import { Component, OnInit, ViewChild } from '@angular/core';
import { EventSettingsModel, ScheduleComponent, PopupOpenEventArgs } from '@syncfusion/ej2-angular-schedule';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {
  @ViewChild('scheduleObj')
  public scheduleObj!: ScheduleComponent;
  public timeScale: Object = { interval: 60, slotCount: 2 };

  public selectedDate: Date = new Date();
  public eventSettings: EventSettingsModel = {
    dataSource: [
      {
        Id: 1,
        Subject: 'Board Meeting',
        StartTime: new Date(2024, 0, 15, 9, 0),
        EndTime: new Date(2024, 0, 15, 11, 0)
      },
      {
        Id: 2,
        Subject: 'Training Session',
        StartTime: new Date(2024, 0, 16, 12, 0),
        EndTime: new Date(2024, 0, 16, 13, 30)
      }
    ]
  };


  constructor() { }

  ngOnInit(): void { }

  public onPopupOpen(args: PopupOpenEventArgs): void {
    if (args.type === 'Editor') {
      // Cast repeatElement and timezoneElement to HTMLElement to access the style property
      let repeatElement = args.element.querySelector('.e-recurrenceeditor') as HTMLElement;
      if (repeatElement) {
        repeatElement.style.display = 'none';
      }

      let timezoneElement = args.element.querySelector('.e-timezonecontainer') as HTMLElement;
      if (timezoneElement) {
        timezoneElement.style.display = 'none';
      }
    }
  }
}
