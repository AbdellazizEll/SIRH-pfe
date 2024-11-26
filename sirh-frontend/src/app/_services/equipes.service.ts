import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";


const base_URL = ["http://localhost:8085/equipe"]
@Injectable({
  providedIn: 'root'
})
export class EquipesService {



  constructor(private http:HttpClient , ) { }


  getEquipes(name: string = '', page: number = 0, size: number = 5): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.get<any>(`${base_URL}/page?name=${name}&page=${page}&size=${size}`, { headers });
  }



  assignManagerToEquipe(idManager: number, idEquipe: number): Observable<any> {
    return this.http.put(`${base_URL}/assignManager/${idManager}/${idEquipe}`, null);  // The second argument is the request body, which is set to null
  }



  assignToEquipe(collaboratorId: number, equipeId: number): Observable<any> {
    return this.http.put(`${base_URL}/assign-to-equipe`, {
      id: collaboratorId,
      equipe: {id_equipe: equipeId}
    });

  }

  addEquipe(equipe: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post<any>(`${base_URL}/createEquipe`, equipe, { headers });
  }


  assignEquipeDepartment(idEquipe:number, idDep:number)
  {
    return this.http.put(`${base_URL}/assignEquipeToDepartment/${idEquipe}/${idDep}`,null)
  }

  addEquipeDepartment(equipe: any, depId:number): Observable<any>
  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post<any>(`${base_URL}/departments/${depId}/add-team`, equipe, { headers });
  }
  getEquipeById(equipeId: any) {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.get<any>( `${base_URL}/findByEquipe/${equipeId}`, {headers})
  }

  assignCollaboratorsToTeam(teamId: number, collaboratorIds: number[]) {

    return this.http.patch<void>(`${base_URL}/assign-list-collaborators/${teamId}`, collaboratorIds);


  }

  getEquipeByManager(id:number):Observable<any>
  {
    return this.http.get<any>(`${base_URL}/getByManagerId/${id}`)
  }
  updateTeam(teamId:number , team:any): Observable<any> {

    return this.http.put<void>(`${base_URL}/${teamId}`, team);
  }

  removeTeam(teamId:number): Observable<any>
  {
    return this.http.delete(`${base_URL}/${teamId}`)
  }

  unassignCollaborator(collaboratorId: number, equipeId: number) {
    return this.http.put<any>(`${base_URL}/${equipeId}/collaborators/${collaboratorId}`, {});

  }

  unassignManager(managerId:number, equipeId:number):Observable<any>

  {
    return this.http.put<any>(`${base_URL}/${equipeId}/manager/${managerId}`, {});

  }

}
