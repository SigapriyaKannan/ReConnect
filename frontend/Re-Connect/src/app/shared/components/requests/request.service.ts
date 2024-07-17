import { HttpClient,HttpHeaders,HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';


export interface Request {
    name: string;
    userId: number;
    profile: string;
}

@Injectable({
  providedIn: 'root'
})
export class RequestService 
{
  constructor(private http: HttpClient) { }

  getPendingRequestForReferent() 
  {
    return this.http.get<Request[]>(`${environment.API}getPendingRequestForReferent`);
  }

  getAcceptedConnections() {
    return this.http.get<Request[]>(`${environment.API}getAcceptedConnections`);
  }

  getPendingRequestForReferrer() 
  {
    return this.http.get<Request[]>(`${environment.API}getPendingRequestForReferrer`);
  }

  acceptRequest(referentId: number) 
  {
    return this.http.post<undefined>(`${environment.API}requestAccepted/${referentId}`,null);
  }

  requestRejected(referentId: number) 
  {
    return this.http.post<undefined>(`${environment.API}requestRejected/${referentId}`,null);
  }

  
}

