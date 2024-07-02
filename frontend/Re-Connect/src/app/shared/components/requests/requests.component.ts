import { Component } from '@angular/core';
import { TabViewChangeEvent, TabViewModule } from 'primeng/tabview';
import { OrderListModule } from 'primeng/orderlist';
import { CommonModule } from '@angular/common';
import { DragDropModule } from '@angular/cdk/drag-drop';

@Component({
  selector: 'rc-requests',
  standalone: true,
  imports: [TabViewModule,DragDropModule,OrderListModule,CommonModule],
  templateUrl: './requests.component.html',
  styleUrl: './requests.component.scss'
})
export class RequestsComponent {

onTabChange(event: any) {
    const index = event.index;
    if (this.userRole === 1) {
      if (index === 0) {
        this.acceptedReferer();
      } else if (index === 1) {
        this.PendingReferer();
      }
    } else if (this.userRole === 0) {
      if (index === 0) {
        this.PendingReferent();
      }
    }
  }

  userRole: number | undefined;
  showList: boolean = true;
  currentList: any[] = [];
  initialTabIndex!: number | 0;
  
  acceptedReferer()
  {
    this.currentList =this.ListOfacceptedReferer
    this.showList = true;
  }

  PendingReferent()
  {
    this.currentList = this.ListOfPendingReferent
    this.showList = true;
  }

  PendingReferer()
  {
    this.currentList = this.ListOfPendingReferer
    this.showList = true;
  }



  ngOnInit(): void {
    this.initialTabIndex =0;
    this.userRole = 1 ; 
    if(this.userRole == 1)
      {
        this.currentList =this.ListOfacceptedReferer
      }else
      {
        this.currentList = this.ListOfPendingReferent
      }
  }




  ListOfPendingReferent: any[] = [
    {
      name: "refereant",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    }
  ]



  ListOfPendingReferer: any[] = [
    {
      name: "pending",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    }
  ]



  ListOfacceptedReferer: any[] = [
    {
      name: "accepted",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    },
    {
      name: "ANBC",
      profile: "profile",
      userId: 1
    }
  ]


}
