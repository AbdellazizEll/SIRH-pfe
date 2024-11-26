import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {ApiResponse} from "../_helpers/api-response";
import {Page} from "../_helpers/page";

@Injectable({
  providedIn: 'root'
})
export class FormationsServiceService {


  private apiUrl = 'http://localhost:8085/formation';  // Replace with your actual API URL

  constructor(private http:HttpClient) { }

  createFormation(formation:any): Observable<any>
  {
    return this.http.post<any>(`${this.apiUrl}`,formation)
  }


  getFormationsByCatalogue(catalogueId:number): Observable<any>
  {
    return this.http.get<HttpResponse<any>>(`${this.apiUrl}/${catalogueId}`)
  }
  getAllFormations(
    query: string = '',
    competenceId: number | null = null,
    catalogueId: number | null = null,
    pageNumber: number = 0
  ): Observable<ApiResponse<Page>> {
    let params = new HttpParams()
      .set('page', pageNumber.toString())
      .set('size', '10'); // Adjust size as needed

    if (query) {
      params = params.set('query', query);
    }
    if (competenceId) {
      params = params.set('competenceId', competenceId.toString());
    }
    if (catalogueId) {
      params = params.set('catalogueId', catalogueId.toString());
    }

    return this.http.get<ApiResponse<Page>>(`${this.apiUrl}/getAllFormations`, { params });
  }


  getFormationById(id:number)
  {
    return this.http.get<any>(`${this.apiUrl}/findById/${id}`)

  }
  findByTargetCompetence(id:number,name:string ='', page:number =0 , size:number = 5):Observable<any>
  {
    return this.http.get<HttpResponse<any>>(`${this.apiUrl}/formations/${id}?name=${name}&page=${page}&size=${size}`)
  }

  deleteFormation(id:number):Observable<any>
  {
    return this.http.delete<HttpResponse<any>>(`${this.apiUrl}/${id}`)
  }

  updateFormation(id:number,formation:any):Observable<any>
  {
    return this.http.put<any>(`${this.apiUrl}/update/${id}`,formation)

  }
  assignCollaborator(request:any):Observable<any>
  {
    return this.http.post<any>(`http://localhost:8085/Formations/assignCollaborator`, request);
  }

  suggestedTrainings(collaboratorId: number, posteId: number, competenceId?: number, currentLevel?: number, targetLevel?: number): Observable<any> {
    // Start building the queryParams
    let queryParams = '';

    // Append each parameter to the query string if it is provided
    if (competenceId) {
      queryParams += `competenceId=${competenceId}&`;
    }
    if (currentLevel) {
      queryParams += `currentLevel=${currentLevel}&`;
    }
    if (targetLevel) {
      queryParams += `targetLevel=${targetLevel}&`;
    }

    // Remove the last '&' if it exists
    queryParams = queryParams.slice(0, -1);

    // If queryParams are not empty, append them to the URL
    const url = queryParams ? `http://localhost:8085/comparison/suggested-trainings/${collaboratorId}/${posteId}?${queryParams}` :
      `http://localhost:8085/comparison/suggested-trainings/${collaboratorId}/${posteId}`;

    // Call the backend using the constructed URL
    return this.http.get(url);
  }

  getTeamSuggestedTraining(
    managerId: number,
    competenceId?: number,
    currentLevel?: number,
    targetLevel?: number
  ): Observable<any> {

    let url = `http://localhost:8085/comparison/suggested-trainings/team/${managerId}`;

    // Check if any of the optional filters are provided and append them to the query parameters
    let queryParams = new URLSearchParams();

    if (competenceId) {
      queryParams.append('competenceId', competenceId.toString());
    }

    if (currentLevel) {
      queryParams.append('currentLevel', currentLevel.toString());
    }

    if (targetLevel) {
      queryParams.append('targetLevel', targetLevel.toString());
    }

    // If queryParams is not empty, append it to the URL
    if (queryParams.toString()) {
      url += `?${queryParams.toString()}`;
    }

    return this.http.get(url);
  }


  getTotalEnrollment():Observable<any>
  {
      return this.http.get(`http://localhost:8085/training-kpis/total-enrollment`)
  }

  countCompletedTraining():Observable<any>
  {
    return this.http.get(`http://localhost:8085/training-kpis/total-completed-trainings`)

  }

  getAverageProgress():Observable<any>
  {
    return this.http.get(`http://localhost:8085/training-kpis/average-progress`)

  }

  getOverallImprovementRate():Observable<any>
  {
    return this.http.get(`http://localhost:8085/training-kpis/overall-competence-improvement-rate`)

  }

  //// ENROLLMENTS
  enrollCollaboratorInTraining(demandeFormationId: number): Observable<any> {
    return this.http.put(`http://localhost:8085/enrollment/${demandeFormationId}`, {});
  }


  // New method to mark training as completed
  markAsCompleted(enrollmentId: number): Observable<any> {
    return this.http.put(`http://localhost:8085/enrollement/mark-completed/${enrollmentId}`, {});
  }

  // New method to update progress for a training
  updateProgressForTraining(collaboratorId: number, formationId: number, progress: number): Observable<any> {
    return this.http.put(`http://localhost:8085/enrollement/${collaboratorId}/${formationId}?progress=${progress}`, {});
  }



  /// KPIS


  getEnrollmentCountByDepartment():Observable<any>
  {
    return this.http.get(`http://localhost:8085/training-kpis/enrollment-count/departments`)

  }
  getOverallCompetenceImprovementRateByDepartment():Observable<any>
  {
    return this.http.get(`http://localhost:8085/training-kpis/overall-competence-improvement-rate/by-department`)

  }


  getTrainingOverview(): Observable<{ completedTrainings: number; inProgressTrainings: number }> {
    return this.http.get<{ completedTrainings: number; inProgressTrainings: number }>(
      `http://localhost:8085/training-kpis/assigned-trainings-overview`
    );
  }


  getTrainingCompletionRate(collaboratorId: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8085/training-kpis/completion-rate/${collaboratorId}`);
  }

  getAverageTrainingProgress(collaboratorId: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8085/training-kpis/average-progress/${collaboratorId}`);
  }


  getTrainingCompletionRateForTeam(teamId: number): Observable<number> {
    return this.http.get<number>(` http://localhost:8085/training-kpis/training/completion-rate/team/${teamId}`);
  }

  // Get Competence Improvement Rates for Team
  getCompetenceImprovementRatesForTeam(teamId: number): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8085/training-kpis/competence-improvement-rate/team/${teamId}`);
  }

  // Get Enrollment Count for Team
  getEnrollmentCountByTeam(teamId: number): Observable<number> {
    return this.http.get<number>(`http://localhost:8085/training-kpis/enrollment-count/team/${teamId}`);
  }

}
