import { Component } from '@angular/core';
import { TabViewChangeEvent, TabViewModule } from 'primeng/tabview';
import { DataViewModule } from 'primeng/dataview';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'rc-requests',
  standalone: true,
  imports: [CommonModule, ButtonModule, TabViewModule, DataViewModule],
  templateUrl: './requests.component.html',
  styleUrl: './requests.component.scss'
})
export class RequestsComponent {
  user: any;
  currentList: any[] = [];
  initialTabIndex!: number | 0;
  imagePath: string = environment.IMAGE_PATH;

  constructor(private activatedRoute: ActivatedRoute) {
    this.activatedRoute.parent?.data.subscribe(({ user }) => {
      this.user = user;
    })
  }

  // onTabChange(event: any) {
  //   const index = event.index;
  //   if (this.userRole === 1) {
  //     if (index === 0) {
  //       this.acceptedReferer();
  //     } else if (index === 1) {
  //       this.PendingReferer();
  //     }
  //   } else if (this.userRole === 0) {
  //     if (index === 0) {
  //       this.PendingReferent();
  //     }
  //   }
  // }


  // acceptedReferer() {
  //   this.currentList = this.ListOfacceptedReferer
  //   this.showList = true;
  // }

  // PendingReferent() {
  //   this.currentList = this.ListOfPendingReferent
  //   this.showList = true;
  // }

  // PendingReferer() {
  //   this.currentList = this.ListOfPendingReferer
  //   this.showList = true;
  // }



  ngOnInit(): void {
    this.initialTabIndex = 0;
    // this.userRole = 1;
    // if (this.userRole == 1) {
    //   this.currentList = this.ListOfacceptedReferer
    // } else {
    //   this.currentList = this.ListOfPendingReferent
    // }
  }

  listOfPendingReferer: any[] = [
    {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    }, {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    }, {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    }, {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    }, {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    }, {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    }, {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    }, {
      name: "Vineeth",
      profile: "profilePicture.png",
      userId: 1
    },
  ]
}
