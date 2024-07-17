import { Component } from '@angular/core';
import { TabViewChangeEvent, TabViewModule } from 'primeng/tabview';
import { DataViewModule } from 'primeng/dataview';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { environment } from '../../../../environments/environment';
import { Request, RequestService } from '../requests/request.service';
import { ToastService } from '../../services/toast.service';
import { ToastModule } from "primeng/toast";

@Component({
  selector: 'rc-requests',
  standalone: true,
  imports: [CommonModule, ButtonModule, TabViewModule, DataViewModule,ToastModule],
  templateUrl: './requests.component.html',
  styleUrl: './requests.component.scss'
})
export class RequestsComponent {
  user: any;
  listOfAccepted: any[] = [];
  listOfPending: any[] = [];
  initialTabIndex!: number | 0;
  imagePath: string = environment.IMAGE_PATH;

  constructor(private activatedRoute: ActivatedRoute,private requestService: RequestService,private toastService: ToastService) {
    this.activatedRoute.parent?.data.subscribe(({ user }) => {
      this.user = user;
      
    })
  }

  // onTabChange(event: any) {
  //   const index = event.index;
  //   if (this.user.id === 1) {
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

  acceptedReferer() {
    this.requestService.getAcceptedConnections().subscribe(
      (response: Request[]) => {
        this.listOfAccepted = response['body'];
      });
  }

  PendingReferent() 
  {
    this.requestService.getPendingRequestForReferrer().subscribe(
      (response: Request[]) => {
        this.listOfPending = response['body'];
      });
  }

  PendingReferer() {
    this.requestService.getPendingRequestForReferent().subscribe(
      (response: Request[]) => {
        this.listOfPending = response['body'];
      });
  }


  ngOnInit(): void {
    this.initialTabIndex = 0;
    if(this.user.role == 1)
      {
        this. acceptedReferer();
        this. PendingReferer();
      }else
      {
        this.PendingReferent();
      }
  }

  // listOfPendingReferer: any[] = [
  //   {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   }, {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   }, {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   }, {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   }, {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   }, {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   }, {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   }, {
  //     name: "Vineeth",
  //     profile: "profilePicture.png",
  //     userId: 1
  //   },
  // ]
}
