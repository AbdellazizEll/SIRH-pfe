import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {TokenStorageService} from "./token-storage.service";
import {Router} from "@angular/router";


const BASE_URL = "http://localhost:8085/api/v1/auth";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loggedIn = new BehaviorSubject<boolean>(this.tokenStorage.isLoggedIn());


  constructor(private http:HttpClient ,private  tokenStorage: TokenStorageService ,
              private router: Router
  ) { }

  get isLoggedIn$(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }
  register(signRequest: any):Observable<any>{
    return this.http.post(BASE_URL+'/register',signRequest)
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(
      `${BASE_URL}/authenticate`,
      { email, password },
      httpOptions
    ).pipe(
      tap(data => {
        if (data.accessToken) {
          this.tokenStorage.saveToken(data.accessToken);
          this.tokenStorage.saveUser(data);
          this.loggedIn.next(true);
        }
      })
    );
  }

  setLoggedIn(status: boolean) {
    this.loggedIn.next(status);
  }

  logout(): void {
    this.http.post(`${BASE_URL}/signout`, {}, httpOptions).subscribe({
      next: () => {
        this.tokenStorage.signOut();
        this.loggedIn.next(false);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout failed', err);
        // Even if logout fails on the server, clear the client state
        this.tokenStorage.signOut();
        this.loggedIn.next(false);
        this.router.navigate(['/login']);
      }
    });
  }

  getCollaborators(name: string='', page: number = 0 ,size: number = 5): Observable<any>
  {
    return this.http.get<any>( `http://localhost:8085/getAllCollaborators?name=${name}&page=${page}&size=${size}`)
}

getUserFromToken(accessToken: string): Observable<any>{
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${accessToken}`
  });

  console.log("Request Headers:", headers);


  return this.http.get( 'http://localhost:8085/currentUser',{headers})
}

  getUser(): any {
    return this.tokenStorage.getUser();
  }




  }

