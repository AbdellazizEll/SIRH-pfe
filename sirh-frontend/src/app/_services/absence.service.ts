import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {DemandeAbsence} from "../domain/DemandeAbsence";
import {NgForm} from "@angular/forms";
import {ApiResponse} from "../_helpers/api-response";
import {Page} from "../_helpers/page";

@Injectable({
  providedIn: 'root'
})
export class AbsenceService {



  private apiUrl = 'http://localhost:8085/demandeAbsence/';  // Replace with your actual API URL
  constructor(private http:HttpClient) { }


    createAbsence(absence:any):Observable<any>
    {
      return this.http.post<any>(`${this.apiUrl}/createAbsence`,absence);
    }

  getAbsenceMotifs(): Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.get<HttpResponse<any>>(`http://localhost:8085/demandeAbsence/getAllMotifs` , {headers});
  }

  addAbsenceRequest(absenceRequest: any, justificatif: File | null): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('dateDebut', absenceRequest.dateDebut);
    formData.append('dateFin', absenceRequest.dateFin);
    formData.append('comment', absenceRequest.comment);
    formData.append('motif', absenceRequest.motif);
    if (justificatif) {
      formData.append('file', justificatif, justificatif.name);
    }
    console.log('Form Data:',formData)
    return this.http.post<any>(`${this.apiUrl}create`, formData);
  }





  approveAbsenceRequestResponsible(demandeId: number) : Observable<any>{

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(`${this.apiUrl}absence-requests-department/${demandeId}/approve`, { headers })
  }



  approveAbsenceRequestManager(demandeId: number): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(`${this.apiUrl}absence-requests/${demandeId}/approve`, { headers });
  }

  approveAbsenceRequestRH(demandeId: number): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(`${this.apiUrl}absence-requests-rh/${demandeId}/approve`, { headers });
  }

  rejectAbsenceRequest(demandeId: number, reason: string): Observable<any> {
    const url = `${this.apiUrl}absence-requests/${demandeId}/reject`;
    const body = { refusalReason: reason };
    return this.http.put<any>(url, body); // Removed responseType
  }




  getAbsencesResponsibleWithFilters(
    responsibleId: number,
    page: number,
    status?: string,
    absenceType?: string,
    startDate?: string,
    endDate?: string
  ): Observable<ApiResponse<Page>> {
    let params = new HttpParams()
      .set('page', page.toString())

    if (status) params = params.set('status', status);
    if (absenceType) params = params.set('absenceType', absenceType);
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<ApiResponse<Page>>(`${this.apiUrl}responsible/absence-requests/${responsibleId}`, { params });
  }

  getAbsencesManagerWithFilters(
    managerId: number,
    page: number,
    status?: string,
    absenceType?: string,
    startDate?: string,
    endDate?: string
  ): Observable<ApiResponse<Page>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', '10');

    if (status) params = params.set('status', status);
    if (absenceType) params = params.set('absenceType', absenceType);
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    // Correct endpoint for Manager
    return this.http.get<ApiResponse<Page>>(`${this.apiUrl}manager/absence-requests/${managerId}`, { params });
  }

  getAbsencesRHWithFilters(
    page: number,
    status?: string,
    absenceType?: string,
    startDate?: string,
    endDate?: string
  ): Observable<ApiResponse<Page>> {
    let params = new HttpParams()
      .set('page', page.toString());

    if (status) params = params.set('status', status);
    if (absenceType) params = params.set('absenceType', absenceType);
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<ApiResponse<Page>>(`${this.apiUrl}absence-requests-rh`, { params });
  }

  deleteAbsence(id:number) : Observable<any>
  {
    return this.http.delete(`${this.apiUrl}delete/${id}`)
  }

  getMyAbsencesWithFilters(
    page: number,
    status?: string,
    absenceType?: string,
    startDate?: string,
    endDate?: string
  ): Observable<ApiResponse<Page>> {
    let params = new HttpParams().set('page', page.toString());

    if (status) params = params.set('status', status);
    if (absenceType) params = params.set('absenceType', absenceType);
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);

    return this.http.get<ApiResponse<Page>>(`${this.apiUrl}my-absence-requests`, { params });
  }

/// KPI


  getAbsenceRate():Observable<any>
  {
  return this.http.get(`http://localhost:8085/absence-kpis/absence-rate`);
  }

getAverageAbsenceDuration():Observable<any>
{
  return this.http.get(`http://localhost:8085/absence-kpis/average-absence-duration`)

}

getAbsenceByDepartment():Observable<any>
{
  return this.http.get(`http://localhost:8085/absence-kpis/absenteeism-by-department`);
}
  getAbsenceRateByDepartment():Observable<any>
  {
    return this.http.get(`http://localhost:8085/absence-kpis/absence-rate-by-department`);
  }

topAbsenceReasons():Observable<any>
{
  return this.http.get(`http://localhost:8085/absence-kpis/top-reasons`)
}


  getLeaveBalance(): Observable<{ leaveBalance: number }> {
    return this.http.get<{ leaveBalance: number }>(
      `http://localhost:8085/absence-kpis/leave-balance`
    );
  }


  getAbsenteismRateManager(collabId:number): Observable<any>
  {
    return this.http.get(`http://localhost:8085/absence-kpis/absenteeism-rate/${collabId}`);
  }

  getAverageAbsenceManager(collabId:number): Observable<any>
  {
    return this.http.get(`http://localhost:8085/absence-kpis/average-absence-duration/${collabId}`);
  }



  /// Responsible KPIs


  getTotalAbsenceDays() {
    return this.http.get(`http://localhost:8085/absence-kpis/total-absence-days`);
  }

  getAbsenceTrends() {
    return this.http.get(`http://localhost:8085/absence-kpis/absence-trends`);
  }
}
