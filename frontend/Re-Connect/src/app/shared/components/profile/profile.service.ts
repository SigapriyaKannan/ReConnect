import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDetails } from './user-details.model';
import {environment} from "../../../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class ProfileService {

    private apiUrl: string = environment.API + 'profile';

    constructor(private http: HttpClient) { }

    getUserDetails(): Observable<UserDetails> {
        return this.http.get<UserDetails>(`${this.apiUrl}`);
    }

    updateUserDetails(userDetailsRequest: any): Observable<UserDetails> {
        return this.http.post<UserDetails>(`${this.apiUrl}/update`, userDetailsRequest);
    }

    uploadProfilePicture(file: File): Observable<void> {
        const formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http.post<void>(`${this.apiUrl}/uploadProfilePicture`, formData);
    }

    uploadResume(file: File): Observable<void> {
        const formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http.post<void>(`${this.apiUrl}/uploadResume`, formData);
    }

    getResume(): Observable<Blob> {
        return this.http.get(`${this.apiUrl}/resume`, { responseType: 'blob' });
    }

    getProfilePicture(): Observable<Blob> {
        return this.http.get(`${this.apiUrl}/profilePicture`, { responseType: 'blob' });
    }
}
