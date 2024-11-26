import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {TokenStorageService} from "./token-storage.service";
import {Collaborator} from "../domain/collaborator";
import * as http from "http";
const API_URL = 'http://localhost:8085/api/v1/demo/'
@Injectable({
  providedIn: 'root'
})
export class CollaboratorService {

  constructor(private http: HttpClient,private tokenStorage: TokenStorageService) {


  }

  getPublicContent(): Observable<any> {
    return this.http.get(API_URL +"collaborator",{ responseType: 'text' });
  }

  getGestRHBoard(): Observable<any> {
    return this.http.get(API_URL + "gestrh",{ responseType: 'text' });
  }

  getManagerBoard(): Observable<any> {
    return this.http.get(API_URL + "manager", { responseType: 'text' });
  }

  getAdminBoard(): Observable<any> {
    return this.http.get(API_URL  +"admin",{ responseType: 'text' });
  }


  userUpdate(id: number,collaborator: any): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.put<any>(`http://localhost:8085/updateCollaborator/${id}`, collaborator);
  }


  public findById(id: number): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.get<any>(`http://localhost:8085/findByCollaborator/${id}`, { headers });
  }

  fetchUserRoles(id:number):Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.get(`http://localhost:8085/fetchRolesForCollab/${id}`,{headers})

  }

  changePassword(request: any): Observable<any> {
    return this.http.patch(`http://localhost:8085/change-password`, request);
  }


  updateUser(id:number,collab : any): Observable<HttpResponse<Collaborator>>{


    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put<HttpResponse<Collaborator>>(`http://localhost:8085/updateCollaborator/${id}`,collab , {headers});
  }

  assignRole(id:number, roleId:number) : Observable<HttpResponse<any>>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<HttpResponse<any>>(`http://localhost:8085/assignrole/${id}/${roleId}`,{headers})
  }

  availableRoles(id:number) : Observable<any>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.get<HttpResponse<any>>(`http://localhost:8085/fetchUserCollab/${id}`,{headers})
  }

  getAllRoles(): Observable<any>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return  this.http.get<HttpResponse<any>>(`http://localhost:8085/getRoles`,{headers})
  }

  removeRole(id:number , roleId:number): Observable<any>
  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(`http://localhost:8085/removerole/${id}/${roleId}`, {headers})
  }

  fetchManagerEquipe():Observable<any>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });


    return this.http.get<any>(`http://localhost:8085/equipe-managers`,{headers});
  }

  fetchResponsiblesAvailable():Observable<any>
  {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.get<any>(`http://localhost:8085/department-responsibles`)

  }

  fetchJoblessCollaborators():Observable<any>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });


    return this.http.get<any>(`http://localhost:8085/collaborators/jobless`,{headers});
  }

  fetchJoblessManagerEquipe():Observable<any>
  {
    return this.http.get<any>(`http://localhost:8085/managers/jobless`);
  }


  unAssignManager(equipeId: number, collaboratorId: number) {
    return this.http.put<any>(`http://localhost:8085/unAssignManager/${equipeId}/${collaboratorId}`, {});
  }

  unAssignResponsible(depId: number, collaboratorId: number) {
    return this.http.put<any>(`http://localhost:8085/unAssignResponsible/${depId}/${collaboratorId}`, {});

  }
  fetchTeamlessCollaborators():Observable<any>
  {
    return this.http.get<any>(`http://localhost:8085/collaborators/teamless`,);

  }




  MostAbsenceCollab():Observable<any>
  {
    return this.http.get<any>(`http://localhost:8085/collaborateur-kpi/most-absences`)
  }


  LeastAbsenceCollab():Observable<any>
  {
    return this.http.get<any>(`http://localhost:8085/collaborateur-kpi/least-absences`)
  }

  TrainingCompleted():Observable<any>
  {
    return this.http.get<any>(`http://localhost:8085/collaborateur-kpi/highest-training-completion`)
  }

  highestCompetenceGrowth():Observable<any>
  {
    return this.http.get<any>(`http://localhost:8085/collaborateur-kpi/highest-competence-growth`)
  }


}
