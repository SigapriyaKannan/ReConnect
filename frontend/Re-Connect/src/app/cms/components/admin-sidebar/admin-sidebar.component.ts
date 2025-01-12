import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api/menuitem';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';

@Component({
  selector: 'rc-admin-sidebar',
  standalone: true,
  imports: [MenuModule, ButtonModule],
  templateUrl: './admin-sidebar.component.html',
  styleUrl: './admin-sidebar.component.scss'
})
export class AdminSidebarComponent implements OnInit {
  items: MenuItem[] | undefined;

  constructor(private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.items = [
      {
        label: "Dashboard",
        icon: "pi pi-home",
        route: "/admin/dashboard"
      },
      {
        label: "Countries",
        icon: "pi pi-globe",
        route: "/admin/countries"
      },
      {
        label: "Cities",
        icon: "pi pi-map-marker",
        route: "/admin/cities"
      },
      {
        label: "Users",
        icon: "pi pi-users",
        route: "/admin/users"
      },
      {
        label: "Companies",
        icon: "pi pi-building",
        route: "/admin/companies",
      }
    ]
  }
}
