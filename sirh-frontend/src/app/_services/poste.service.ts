import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PosteService {

  private apiUrl = 'http://localhost:8085/poste';

  constructor(private http: HttpClient) { }

  createPoste(poste: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/create`, poste);
  }

  updatePoste(id: number, poste: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, poste);
  }

  GetCompetenceComparison(collaboratorId: number , posteId: number): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/comparison/${collaboratorId}/${posteId}`)
  }

  getPosteById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  deletePoste(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  addCompetenceToPoste(posteId: number, competenceId: number, evaluation: string): Observable<any> {
    const params = new HttpParams().set('evaluation', evaluation);
    return this.http.post<any>(`${this.apiUrl}/Poste-Competence/${posteId}/${competenceId}`, {},{params});
  }



  getPosteByCompetence(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/Poste-competence/competence/${id}`);
  }

  getCompetenceByType(id: number, scaleType: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/Poste-competence/getByType/${id}`, { params: { scaleType } });
  }
  currentPosteCompetence(posteId:number):Observable<any>
  {
    return this.http.get(`${this.apiUrl}/current/${posteId}`);
  }


  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/Poste-competence/${id}`);
  }

  getAllPostes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}`);
  }

  compareCollaborateurWithPoste(collaborateurId: number, posteId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/compare/${collaborateurId}/${posteId}`);
  }

  assignPoste(collaborateurId: number, posteId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${collaborateurId}/assignPoste/${posteId}`, {});
  }

  unassignCollaborator(collaboratorId: number, posteId: number) {
    return this.http.put<any>(`${this.apiUrl}/postes/${posteId}/collaborators/${collaboratorId}`, {});

  }
}
