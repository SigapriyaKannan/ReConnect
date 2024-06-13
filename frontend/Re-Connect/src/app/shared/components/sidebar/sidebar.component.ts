import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'rc-sidebar',
  standalone: true,
  imports: [MenuModule, ButtonModule, TooltipModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  items: MenuItem[] | undefined;

  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.items = [
      {
        label: "Homepage",
        icon: "pi pi-home",
        route: "/homepage"
      },
      {
        label: "Notifications",
        icon: "pi pi-bell",
        route: "/notifications"
      },
      {
        label: "Messages",
        icon: "pi pi-inbox",
        route: "/messages"
      },
      {
        label: "Requests",
        icon: "pi pi-users",
        route: "/requests"
      }
    ]
  }
}
