import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CatalogueServiceService {


  private apiUrl ='http://localhost:8085/catalogue'
  constructor(private http:HttpClient) { }


  getAllCatalogues(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/getAllCatalogues`);
  }

  createCatalogue(catalogue:any): Observable<any>
  {
    return this.http.post<any>(`${this.apiUrl}/createCatalogue`,catalogue)
  }



  updateCatalogue(id:number,catalogue:any): Observable<any>
  {
    return this.http.put<any>(`${this.apiUrl}/update/${id}`,catalogue)
  }
  findByCatalogueId(id:any): Observable<any>
  {
    return this.http.get<HttpResponse<any>>(`${this.apiUrl}/${id}`)
  }

  getTrainingsByCatalogue(id:any): Observable<any>
  {
    return this.http.get<HttpResponse<any>>(`http://localhost:8085/formation/${id}`)
  }


  deleteCatalogue(id:number):Observable<any>
  {
    return this.http.delete<void>(`${this.apiUrl}/deleteCatalogue/${id}`)
  }
}
