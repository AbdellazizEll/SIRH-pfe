import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CompetenceService {

  private apiUrl = 'http://localhost:8085/competences/';  // Replace with your actual API URL

  constructor(private http:HttpClient) { }



  createCompetence(competence:any): Observable<any>
  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(`${this.apiUrl}create`,competence,{headers})

  }

  getCompetenceById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }


  getScaleTypes():Observable<any>
  {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.get(`${this.apiUrl}scaleTypes`,{headers})


  }
  addCompetenceToCollaborator(collaboratorId: number, competenceId: number, evaluation: string): Observable<any> {
    const params = new HttpParams().set('evaluation', evaluation);
    return this.http.post(`${this.apiUrl}${collaboratorId}/${competenceId}`, {}, { params });
  }

  updateCollaboratorEvaluation(collaboratorId: number, competenceId: number, evaluation: string): Observable<any> {
    const params = new HttpParams().set('evaluation', evaluation);
    return this.http.put(`${this.apiUrl}updateCompetenceCollaborateur/${collaboratorId}/${competenceId}`, {}, { params });
  }


  updateCompetenceEvaluationPoste(posteId: number, competenceId: number, evaluation: string): Observable<any> {
    const params = new HttpParams().set('evaluation', evaluation);
    return this.http.put(`${this.apiUrl}updateCompetencePoste/${posteId}/${competenceId}`, {}, { params });
  }
  findAllCompetence(): Observable<any>
 {
   return this.http.get(`${this.apiUrl}`);
 }

 CurrentUserCompetence(userId:number):Observable<any>
 {
   return this.http.get(`${this.apiUrl}current/${userId}`);
 }


 deleteCompetence(competenceId:number):Observable<any>
 {
   return this.http.delete(`${this.apiUrl}/${competenceId}`)
 }



  getAllCompetences(name: string='', page: number = 0 ,size: number = 5): Observable<any> {
    return this.http.get<HttpResponse<any>>(`${this.apiUrl}getAllCompetence`);
  }

  getCompetenceCoverage(collabId:number):Observable<any> {
    return this.http.get(`http://localhost:8085/competence-kpis/coverage/${collabId}`);
  }

  getCompetenceGap(collabId:number):Observable<any>{
    return this.http.get(`http://localhost:8085/competence-kpis/gap/${collabId}`)
  }


  getcompetenceCoverage(departmentId:number):Observable<any>
  {
    return this.http.get(`http://localhost:8085/competence-kpis/${departmentId}/competence-coverage`)

  }

  getCompetenceImprovementRate():Observable<any>{
    return this.http.get(`http://localhost:8085/competence-kpis/improvement-rate/departments`)
  }


  getEnrollmentCountByDepartment():Observable<any>{
    return this.http.get(`http://localhost:8085/competence-kpis/enrollment-count/departments`)
  }


  getCompetenceGapForAllDepartment():Observable<any>{
    return this.http.get(`http://localhost:8085/competence-kpis/departmentCompetenceGap}`)
  }


  getCompetencyGaps(): Observable<any> {
    return this.http.get<any>(
      `http://localhost:8085/competence-kpis/current-competency-gap`
    );
  }

  //// MANAGER DASHBOARD


  /// RESPONSIBLE DASHBOARD

  getCompetenceCoverageByDepartmentResponsible(departmentId: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8085/competence-kpis/${departmentId}/competence-coverage-responsible`);
  }

  getCompetenceGapsForTeam(teamId: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8085/competence-kpis/team/${teamId}/competence-gaps`);
  }

  getCompetenceCoverageByTeam(teamId: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8085/competence-kpis/team/${teamId}/competence-coverage`);
  }

  // New method to get Competence Improvement Rates for All Competences within a Team
  getCompetenceImprovementRatesForTeam(teamId: number): Observable<any> {
    return this.http.get<any>(`http://localhost:8085/training-kpis/competence-improvement-rate/team/${teamId}`);
  }



}
