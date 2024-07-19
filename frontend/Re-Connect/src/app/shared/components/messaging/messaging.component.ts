import { DatePipe, NgClass } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextareaModule } from "primeng/inputtextarea";
import { ListboxModule } from "primeng/listbox";
import { Message, MessagingService } from './messaging.service';
import { Subscription, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { OverlayService } from '../../services/overlay.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'rc-messaging',
  standalone: true,
  imports: [InputTextareaModule, FormsModule, ButtonModule, DatePipe, NgClass, ListboxModule],
  templateUrl: './messaging.component.html',
  styleUrl: './messaging.component.scss',
  providers: [MessagingService]
})
export class MessagingComponent implements OnInit, OnDestroy {
  message: string = "";
  selectedUser: any = null;
  listOfUsers: any[] = [];
  listOfMessages: Message[] = [];
  fetchingMessages: boolean = false;
  imagePath: string = environment.SOCKET_SERVER;
  messageListener$: Subscription = new Subscription();
  user: any;

  constructor(private messagingService: MessagingService, private overlaySerivce: OverlayService, private actiavteRoute: ActivatedRoute) {
    this.actiavteRoute.parent?.data.subscribe(({ user }) => {
      this.user = user;
    })
  }

  ngOnInit(): void {
    this.overlaySerivce.showOverlay("Fetching Users");
    this.messagingService.getAcceptedConnections().pipe(tap(() => this.overlaySerivce.hideOverlay())).subscribe((response) => {
      this.listOfUsers = response["body"];
    });

    this.messageListener$ = this.messagingService.receiveMessage(this.user.email).subscribe((data: Message) => {
      console.log('Received message:', data);
      if (this.selectedUser.id === data['receiverId']) {
        this.listOfMessages.push(data);
      }
    });
  }

  sendMessage() {
    if (this.message) {
      const messageBody: Message = {
        senderEmail: this.user.email, senderId: this.user.userId, receiverEmail: this.selectedUser.name, receiverId: this.selectedUser.id, message: this.message, timestamp: new Date()
      }
      this.messagingService.sendMessage(messageBody);
      this.listOfMessages.push(messageBody);
      this.message = "";
    }
  }

  fetchMessages() {
    this.fetchingMessages = true;
    this.messagingService.getChatHistory(this.selectedUser.name).pipe(tap(() => this.fetchingMessages = false)).subscribe({
      next: response => {
        this.listOfMessages = response['body'];
      },
      error: () => {
        this.listOfMessages = []
      }
    })
  }

  ngOnDestroy(): void {
    this.messagingService.closeConnection();
    this.messageListener$.unsubscribe();
  }
}
