import { DatePipe, NgClass } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextareaModule } from "primeng/inputtextarea";
import { ListboxModule } from "primeng/listbox";
import { MessagingService } from './messaging.service';
import { tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { OverlayService } from '../../services/overlay.service';

@Component({
  selector: 'rc-messaging',
  standalone: true,
  imports: [InputTextareaModule, FormsModule, ButtonModule, DatePipe, NgClass, ListboxModule],
  templateUrl: './messaging.component.html',
  styleUrl: './messaging.component.scss',
  providers: [MessagingService]
})
export class MessagingComponent implements OnInit {
  value: string = "";
  selectedUser: any = null;
  listOfUsers: any[] = [];
  listOfMessages: any[] = [];
  fetchingMessages: boolean = false;
  imagePath: string = environment.IMAGE_PATH;

  constructor(private messagingService: MessagingService, private overlaySerivce: OverlayService) { }

  ngOnInit(): void {
    this.overlaySerivce.showOverlay("Fetching Users");
    this.messagingService.getAcceptedConnections().pipe(tap(() => this.overlaySerivce.hideOverlay())).subscribe((response) => {
      this.listOfUsers = response["body"];
    });
  }

  addMessage() {
    this.listOfMessages.push({
      message: this.value,
      role: 1,
      userId: 21,
      timestamp: new Date()
    })
    this.value = "";
  }

  fetchMessages() {
    this.fetchingMessages = true;
    this.messagingService.getChatHistory(this.selectedUser.name).pipe(tap(() => this.fetchingMessages = false)).subscribe(response => {
      console.log(response);
    })
  }
}
