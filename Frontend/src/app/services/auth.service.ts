import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class AuthService{

    private apiServerUrl = 'http://localhost:8888';

    constructor(private httpClient: HttpClient){}

    public signUp(requestData: { [key: string]: string }) : Observable<string>{
        return this.httpClient.post<string>(`${this.apiServerUrl}/user/signup`, requestData);
    }

   
}