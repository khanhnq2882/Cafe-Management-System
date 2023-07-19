import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class AuthService{

    private apiServerUrl = 'http://localhost:8888';

    constructor(private httpClient: HttpClient){}

    public register(requestData: { [key: string]: string }) : Observable<string>{
        return this.httpClient.post<string>(`${this.apiServerUrl}/user/register`, requestData);
    }

    public login(requestData: { [key: string]: string }) : Observable<string>{
        return this.httpClient.post<string>(`${this.apiServerUrl}/user/login`, requestData);
    }

   
}