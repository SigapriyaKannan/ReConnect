import { Component } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { AvatarModule } from 'primeng/avatar';
import { MenubarModule } from 'primeng/menubar';

@Component({
  selector: 'rc-header',
  standalone: true,
  imports: [MenubarModule, AvatarModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  items: MenuItem[] | undefined;
  constructor() { }

  ngOnInit(): void {
    this.items = [

    ]
  }
}
