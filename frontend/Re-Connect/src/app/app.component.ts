import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'rc-root',
  standalone: true,
  providers: [MessageService],
  imports: [RouterOutlet, ToastModule],
  template: `
    <router-outlet />
    <p-toast position="bottom-right"/>
  `,
  styles: [],
})
export class AppComponent {
  title = 'Re-Connect';
}
