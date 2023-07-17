import { Component, OnInit } from '@angular/core';
import { UserActivity } from 'src/app/models/userActivity';
import { CurrentService } from 'src/app/services/current.service';
import { MatTableDataSource } from '@angular/material/table';
@Component({
  selector: 'app-users-activity',
  templateUrl: './users-activity.component.html',
  styleUrls: ['./users-activity.component.css']
})
export class UsersActivityComponent implements OnInit {
  dataSource = new MatTableDataSource<any>();
  displayedColumns: string[] = ['name', 'currency', 'requestDate', 'value'];
  constructor(private currentService: CurrentService) { }

  ngOnInit(): void {
    this.loadUserActivity();
  }
  
  loadUserActivity() {
    this.currentService.getUserActivity().subscribe((userActivity: UserActivity[]) => {
      this.dataSource.data = userActivity;
      console.log(this.dataSource.data);
    })
  }
}
