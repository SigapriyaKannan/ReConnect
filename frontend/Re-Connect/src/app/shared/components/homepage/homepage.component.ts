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

  constructor(private searchService: HomepageService) { }

  onSearch() {
    this.hasSearched = true;
    if (this.selectedSearchType.value === 'user') {
      this.searchService.searchUsers(this.searchQuery).subscribe(
        (response) => {
          this.processUserSearchResults(response);
        },
        (error) => {
          console.error('Error searching users', error);
          this.searchResults = [];
        }
      );
    } else {
      this.searchService.searchCompanyUsers(this.searchQuery).subscribe(
        (response) => {
          this.processSearchResults(response);
        },
        (error) => {
          console.error('Error searching company users', error);
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