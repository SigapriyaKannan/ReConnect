import { Component } from '@angular/core';
import { HomepageService } from './homepage.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TabViewModule } from 'primeng/tabview';
import { DataViewModule } from 'primeng/dataview';
import { environment } from '../../../../environments/environment';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { TagModule } from 'primeng/tag';
import { OverlayService } from '../../services/overlay.service';

@Component({
  selector: 'rc-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, DropdownModule, InputTextModule, ButtonModule, TabViewModule, DataViewModule, TagModule],
})
export class HomepageComponent {
  searchQuery: string = '';
  selectedSearchType: any = { name: 'User', value: 'user' };
  searchTypes: any[] = [
    { name: 'User', value: 'user' },
    { name: 'Company', value: 'company' }
  ];
  searchResults: {
    profilePicture: string;
    name: string;
    companyName: string;
    experience: {
      years: number;
      level: string;
    };
    status: string | null;
  }[] = [];
  hasSearched: boolean = false;
  user: any;
  searching: boolean = false;

  constructor(private activatedRoute: ActivatedRoute, private searchService: HomepageService, private overlayService: OverlayService) {
    this.activatedRoute.data.subscribe(({ user }) => { this.user = user });
  }

  getStatusSeverity(status: string): "success" | "danger" | "info" | "warning" {
    switch (status?.toLowerCase()) {
      case 'accepted':
        return "success";
      case 'rejected':
        return "danger";
      case 'pending':
        return "info";
      default:
        return "warning";
    }
  }

  getExperienceLevel(experience: number): string {
    if (experience <= 1) {
      return "Entry Level";
    } else if (experience <= 3) {
      return "Mid Level";
    } else if (experience <= 4) {
      return "Senior Level";
    } else {
      return "Executive Level";
    }
  }

  onSearch() {
    this.hasSearched = true;
    this.searching = true;
    this.overlayService.showOverlay("Searching users");
    if (this.selectedSearchType.value === 'user') {
      this.searchService.searchUsers(this.searchQuery)
        .pipe(finalize(() => { this.searching = false; this.overlayService.hideOverlay(); }))
        .subscribe(
          (response) => {
            this.processUserSearchResults(response);
          },
          (error) => {
            this.searchResults = [];
          }
        );
    } else {
      this.searchService.searchCompanyUsers(this.searchQuery)
      
        .pipe(finalize(() => { this.searching = false; this.overlayService.hideOverlay(); }))
        .subscribe(
          (response) => {
            this.processSearchResults(response);
          },
          (error) => {
            this.searchResults = [];
          }
        );
    }
  }
  private processUserSearchResults(response: any) {
    console.log('User search raw response:', response);
    if (response && response.data && Array.isArray(response.data)) {
      this.searchResults = response.data.map((user: any) => ({
        profilePicture: user.profilePicture,
        name: user.userName,
        companyName: user.companyName,
        experience: {
          years: user.experience,
          level: this.getExperienceLevel(user.experience)
        },
        status: user.status
        
      }));
    } else {
      console.error('Unexpected response format', response);
      this.searchResults = [];
    }
  }

  private processSearchResults(response: any) {
    if (response && response.data && Array.isArray(response.data)) {
      this.searchResults = response.data.map((companyUser: any) => ({
        profilePicture: companyUser.profilePicture,
        name: companyUser.userName,
        companyName: companyUser.companyName,
        experience: {
          years: companyUser.experience,
          level: this.getExperienceLevel(companyUser.experience)
        },
        status: companyUser.status

      }));
    } else {
      console.error('Unexpected response format', response);
      this.searchResults = [];
    }
  }
}