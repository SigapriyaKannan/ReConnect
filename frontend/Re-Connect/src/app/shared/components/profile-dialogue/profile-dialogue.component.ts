import { Component } from '@angular/core';

@Component({
  selector: 'rc-profile-dialogue',
  standalone: true,
  imports: [],
  templateUrl: './profile-dialogue.component.html',
  styleUrl: './profile-dialogue.component.scss'
})
export class ProfileComponent {
  displayProfileDialog: boolean = false;
  userDetails: any = {
    userName: 'John Doe',
    experience: 5,
    companyName: 'TechCorp',
    cityName: 'New York',
    countryName: 'USA',
    skills: [{ skillName: 'Angular' }, { skillName: 'JavaScript' }]
  };

  showProfileDialog() {
    this.displayProfileDialog = true;
  }
}