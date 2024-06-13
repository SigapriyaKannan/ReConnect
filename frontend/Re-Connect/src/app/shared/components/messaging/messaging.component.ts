import { DatePipe, NgClass } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextareaModule } from "primeng/inputtextarea";

@Component({
  selector: 'rc-messaging',
  standalone: true,
  imports: [InputTextareaModule, FormsModule, ButtonModule, DatePipe, NgClass],
  templateUrl: './messaging.component.html',
  styleUrl: './messaging.component.scss'
})
export class MessagingComponent {
  value: string = "";
  listOfMessages: any[] = [{
    message: "Hello",
    timestamp: "10/06/2024 14:23",
    role: 0,
    userId: 12
  },
  {
    message: "Hi",
    timestamp: "10/06/2024 14:21",
    role: 1,
    userId: 21
  }]

  addMessage() {
    this.listOfMessages.push({
      message: this.value,
      role: 1,
      userId: 21,
      timestamp: new Date()
    })
    this.value = "";
  }
}
