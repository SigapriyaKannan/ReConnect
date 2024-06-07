import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api/menuitem';
import { MenuModule } from 'primeng/menu';

@Component({
  selector: 'rc-admin-sidebar',
  standalone: true,
  imports: [MenuModule],
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
        command: () => {
          this.router.navigate(["dashboard"], { relativeTo: this.route });
        }
      },
      {
        label: "Countries",
        command: () => {
          this.router.navigate(["countries"], { relativeTo: this.route });
        }
      },
      {
        label: "Cities",
        command: () => {
          this.router.navigate(["cities"], { relativeTo: this.route });
        }
      },
      {
        label: "Users",
        command: () => {
          this.router.navigate(["users"], { relativeTo: this.route });
        }
      },
      {
        label: "Companies",
        command: () => {
          this.router.navigate(["companies"], { relativeTo: this.route });
        }
      }
    ]
  }
}
