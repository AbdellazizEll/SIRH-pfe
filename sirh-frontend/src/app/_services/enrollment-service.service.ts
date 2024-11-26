import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class EnrollmentServiceService {

  constructor(private http:HttpClient ) { }


  getEnrollmentsByCollaborator(collaboratorId: number, page: number=0, size: number=5): Observable<any> {
    return this.http.get(`http://localhost:8085/enrollment/collaborator/${collaboratorId}?page=${page}&size=${size}`);
  }

  rejectEnrollment(id: number, rejectionReason: string): Observable<void> {
    return this.http.put<void>(`http://localhost:8085/formations/Demande/reject/${id}`, rejectionReason);
  }


  markAsCompleted(id:number):Observable<any>
  {

    return this.http.put<any>(`http://localhost:8085/enrollment/mark-completed/${id}`,null);
  }


  // Update the progress of a specific training
  updateProgressForTraining(collaboratorId: number, formationId: number, progress: number): Observable<any> {
    const url = `http://localhost:8085/enrollment/${collaboratorId}/${formationId}?progress=${progress}`;
    console.log('API URL:', url); // Debug the URL
    return this.http.put(url, {});
  }


  removeEnrollment(id:number):Observable<any>
  {
    return this.http.delete(`http://localhost:8085/enrollment/remove/${id}`);
  }


  getCompetenceHistory(id:number):Observable<any>
  {
    return this.http.get(`http://localhost:8085/enrollment/competence-history/${id}`);
  }


  evaluateTrainingCompletion(enrollmentId: number, isApproved: boolean, rejectionReason?: string): Observable<void> {
    return this.http.put<void>(`http://localhost:8085/enrollment/evaluate/${enrollmentId}`, {
      isApproved,
      rejectionReason
    });
  }
}
