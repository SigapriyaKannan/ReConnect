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
import { OverlayService } from '../../services/overlay.service';

@Component({
  selector: 'rc-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, DropdownModule, InputTextModule, ButtonModule, TabViewModule, DataViewModule]
})
export class HomepageComponent {
  searchQuery: string = '';
  selectedSearchType: any = { name: 'User', value: 'user' };
  searchTypes: any[] = [
    { name: 'User', value: 'user' },
    { name: 'Company', value: 'company' }
  ];
  searchResults: any[] = [];
  hasSearched: boolean = false;
  user: any;
  searching: boolean = false;

  constructor(private activatedRoute: ActivatedRoute, private searchService: HomepageService, private overlayService: OverlayService) {
    this.activatedRoute.data.subscribe(({ user }) => { this.user = user });
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
    if (response && response.data && Array.isArray(response.data)) {
      this.searchResults = response.data.map((user: any) => ({
        name: user.userName,
        companyName: user.companyName
      }));
    } else {
      console.error('Unexpected response format', response);
      this.searchResults = [];
    }
  }

  private processSearchResults(response: any) {
    if (response && response.data && Array.isArray(response.data)) {
      this.searchResults = response.data.map((email: string) => ({
        name: email
      }));
    } else {
      console.error('Unexpected response format', response);
      this.searchResults = [];
    }
  }
}