import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'rc-container-layout',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './container-layout.component.html',
  styleUrl: './container-layout.component.scss'
})
export class ContainerLayoutComponent {
}
