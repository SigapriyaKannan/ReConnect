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

  acceptedRequest() {
    this.requestService.getAcceptedRequest().subscribe(
      (response: Request[]) => {
        this.listOfAccepted = response['body'];
      });
  }

  pendingRequest(role : number) 
  {
    this.requestService.getPendingRequest(role).subscribe(
      (response: Request[]) => {
        this.listOfPending = response['body'];
      });
  }

  acceptRequest(referentId: number) {
    this.requestService.acceptRequest(referentId).subscribe(
      (response) => {
        this.toastService.showSuccess('Request accepted successfully!');
        this.pendingRequest(this.user.role)
      },
      (error) => {
        this.toastService.showError('Sonething Went Wrong!');
      }
    );
}

requestRejected(referentId: number) {
  this.requestService.requestRejected(referentId).subscribe(
    (response) => {
      this.toastService.showSuccess('Request rejected successfully!');
      this.pendingRequest(this.user.role)
    },
    (error) => {
      this.toastService.showError('Sonething Went Wrong!');
    }
  );
}


  ngOnInit(): void {
    this.initialTabIndex = 0;
    if(this.user.role == 1)
      {
        this. acceptedRequest();
      }
      this. pendingRequest(this.user.role);
  }
}
