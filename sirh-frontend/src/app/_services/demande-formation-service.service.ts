import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DemandeFormationServiceService {

  private apiUrl = 'http://localhost:8085/formations/Demande';  // Replace with your actual API URL


  constructor(private http:HttpClient) { }

  getAllRequests(status?: string, competenceId?: number, page: number = 0, size: number = 10) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (status && status.trim() !== '') {
      params = params.set('status', status);
    }

    if (competenceId !== undefined && competenceId !== null) {
      params = params.set('competenceId', competenceId.toString());
    }

    return this.http.get<any>('http://localhost:8085/formations/getAllRequests', { params });
  }
  getRequestsByStatusRH(status: string = '', page: number = 0, size: number = 5): Observable<any> {
    let url = `${this.apiUrl}/status?page=${page}&size=${size}&status=${status}`;

    return this.http.get(url);
  }

  createCustomRequest(request:any): Observable<any>
  {
    return this.http.post<any>(`${this.apiUrl}/createCustomized`,request);
  }


  createDemande(demande:any): Observable<any>
  {
    return this.http.post<any>(`${this.apiUrl}`,demande)
  }


  getRequestTrainingById(id:number): Observable<any>
  {
    return this.http.get<HttpResponse<any>>(`${this.apiUrl}/${id}`)
  }

  addEnrollment(id:number): Observable<any>
  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(`http://localhost:8085/enrollment/${id}`,{headers})
  }

  approveDemande(id:number):Observable<any>
  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(`${this.apiUrl}/approve/${id}`,{headers})
  }

  rejectDemandeTraining(id:number, rejectionReason:string): Observable<any>
  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(`${this.apiUrl}/reject/${id}`,rejectionReason,{headers})
  }

  getHistoryOfTraining( managerId:number, status?: string, competenceId?: number, page: number = 0, size: number = 10) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (status && status.trim() !== '') {
      params = params.set('status', status);
    }

    if (competenceId !== undefined && competenceId !== null) {
      params = params.set('competenceId', competenceId.toString());
    }

    return this.http.get<any>(`http://localhost:8085/formations/Demande/managerHistory/${managerId}`, { params });
  }

  getRequestByStatus(managerId:number,status:string , page: number = 0 , size:number = 5 ):Observable<any>
  {
    return this.http.get<HttpResponse<any>>(`${this.apiUrl}/managerHistory/${managerId}?status=${status}&?page=${page}&size=${size}`);
  }
  deleteRequest(id:number):Observable<any>
  {
    return this.http.delete<HttpResponse<any>>(`${this.apiUrl}/${id}`)
  }

  getRequestById(id:number):Observable<any>
  {
    return this.http.get<any>(` http://localhost:8085/Formations/findById/${id}`)
  }


  updateRequest(id:number,modifiedRequest: any) {

    return this.http.put<any>(`${this.apiUrl}/update/${id}`,modifiedRequest)
  }

  createRequestFromSuggested(request:any): Observable<any> {
    return this.http.post(`http://localhost:8085/formations/create-from-suggested`,request);
  }
}
