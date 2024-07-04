import { Injectable } from "@angular/core";
import { MessageService } from 'primeng/api';

@Injectable({
    providedIn: "root"
})
export class ToastService {
    constructor(private messageService: MessageService) { }

    showFormError() {
        this.messageService.clear();
        this.messageService.add({
            severity: "error",
            summary: "Form Error",
            detail: "Error in form!"
        })
    }

    showSuccess(message: string) {
        this.messageService.clear();
        this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: message
        })
    }

    showError(message: string) {
        this.messageService.clear();
        this.messageService.add({
            severity: "error",
            summary: "Error",
            detail: message
        })
    }
}