import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DepartmentService {

  constructor(private http:HttpClient) { }


  getDepartments(name: string='', page: number = 0 , size: number =5): Observable<any>
  {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.get<HttpResponse<any>>(`http://localhost:8085/department/page?name=${name}&page=${page}&size=${size}`, {headers})
  }


  assignManagerToDepartment(departmentId:number,collaboratorId:number): Observable<void>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<void>(`http://localhost:8085/department/assign-manager/${departmentId}/${collaboratorId}`, { headers });
  }

  addDepartment(department:any): Observable<any>
  {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<any>(`http://localhost:8085/department`,department,{headers} )
  }

  getDepartmentById(departmentId: number): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.get<any>(`http://localhost:8085/department/${departmentId}`, { headers });
  }

  unAssignResponsible(departmentId: number, collaboratorId: number) {
    return this.http.put<any>(`http://localhost:8085/department/${departmentId}/collaborators/${collaboratorId}`, {});

  }
  updateDepartmentDetails(departmentId: number, departmentDetails: any) {
    return this.http.put<any>(`http://localhost:8085/department/${departmentId}`, departmentDetails);
  }


}

