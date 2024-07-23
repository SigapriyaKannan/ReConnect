import { Component } from '@angular/core';
import { TabViewChangeEvent, TabViewModule } from 'primeng/tabview';
import { DataViewModule } from 'primeng/dataview';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { environment } from '../../../../environments/environment';
import { Request, RequestService } from '../requests/request.service';
import { ToastService } from '../../services/toast.service';
import { ToastModule } from "primeng/toast";

@Component({
  selector: 'rc-requests',
  standalone: true,
  imports: [CommonModule, ButtonModule, TabViewModule, DataViewModule, ToastModule, RouterLink],
  templateUrl: './requests.component.html',
  styleUrl: './requests.component.scss'
})
export class RequestsComponent {
  user: any;
  listOfAccepted: any[] = [];
  listOfPending: any[] = [];
  initialTabIndex!: number | 0;
  imagePath: string = environment.SERVER;

  constructor(private activatedRoute: ActivatedRoute, private requestService: RequestService, private toastService: ToastService) {
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

  pendingRequest() {
    this.requestService.getPendingRequest().subscribe(
      (response: Request[]) => {
        this.listOfPending = response['body'];
      });
  }


  updateRequest(userId: number, status: boolean) {
    this.requestService.updateRequest(userId, status).subscribe(
      (response) => {
        if (status) {
          this.toastService.showSuccess('Request accepted successfully!');
        } else {
          this.toastService.showSuccess('Request rejected successfully!');
        }
        this.pendingRequest()
      },
      (error) => {
        this.toastService.showError('Sonething Went Wrong!');
      }
    );
  }




  ngOnInit(): void {
    this.initialTabIndex = 0;
    if (this.user.role == 1) {
      this.acceptedRequest();
    }
    this.pendingRequest();
  }
}
