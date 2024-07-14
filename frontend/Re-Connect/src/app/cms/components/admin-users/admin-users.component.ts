
import { Component, OnInit } from '@angular/core';
import { UserService, UserNameTypeIdDTO } from '../../../shared/services/user.service';
import { DialogModule } from 'primeng/dialog';
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'rc-admin-users',
  standalone: true,
  imports: [ DialogModule, NgIf, NgForOf ],
  templateUrl: './admin-users.component.html',
  styleUrls: ['./admin-users.component.scss']
})
export class AdminUsersComponent implements OnInit {
  users: UserNameTypeIdDTO[] = [];
  displayDeleteDialog = false;
  userToDelete: UserNameTypeIdDTO | null = null;

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.loadUsers('Referent');
  }

  loadUsers(typeName: string) {
    this.userService.getUserNamesAndTypeIdsByUserType(typeName).subscribe(
      (users) => this.users = users,
      (error) => console.error('Error loading users:', error)
    );
  }

  showDeleteUserConfirmation(user: UserNameTypeIdDTO) {
    this.userToDelete = user;
    this.displayDeleteDialog = true;
  }

  hideDeleteDialog() {
    this.displayDeleteDialog = false;
    this.userToDelete = null;
  }

  onDeleteUser() {
    if (this.userToDelete) {
      this.userService.deleteUser(this.userToDelete.userId).subscribe(
        () => {
          this.loadUsers('Admin');
          this.hideDeleteDialog();
        },
        (error) => console.error('Error deleting user:', error)
      );
    }
  }
}