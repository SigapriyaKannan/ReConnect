import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { AvatarModule } from "primeng/avatar";

@Component({
  selector: 'rc-admin-header',
  standalone: true,
  imports: [MenubarModule, AvatarModule],
  templateUrl: './admin-header.component.html',
  styleUrl: './admin-header.component.scss'
})
export class AdminHeaderComponent implements OnInit {
  items: MenuItem[] | undefined;
  constructor() { }

  ngOnInit(): void {
    this.items = [

    ]
  }
}
