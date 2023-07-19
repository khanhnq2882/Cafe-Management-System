import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { NgForm, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { StorageService } from '../services/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, AfterViewInit{
  @ViewChild("loginForm", { static: false }) loginForm!: NgForm;

  isSuccessful = false;
  isLoginFailed = false;
  isLoggedIn = false;
  errorMessage = '';

  constructor(private authService: AuthService, private storageService: StorageService){
  }
  ngAfterViewInit(): void {
    setTimeout(() => {
      this.loginForm.controls["email"].addValidators([
        Validators.required,
        Validators.pattern("^[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}$")
      ]); 
      this.loginForm.controls["email"].updateValueAndValidity;

      this.loginForm.controls["password"].addValidators([
        Validators.required,
        Validators.pattern("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[ !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]).{8,25}$")
      ]); 
      this.loginForm.controls["password"].updateValueAndValidity;
    }, 0);    
  }

  ngOnInit(): void {
    
  }

  onSubmit(){
    const requestData = {
      email: this.loginForm.value.email,
      password: this.loginForm.value.password,
    };
    console.log(this.loginForm.value);
    this.authService.login(requestData).subscribe({
      next: data =>{
        console.log(data);
        this.storageService.saveUser(data);
        this.isLoginFailed = false;
        this.isLoggedIn = true;
        // this.reloadPage();
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }

    });

  }

  // private isButtonDisabled = false;
  // loginButtonDisabled(): boolean {
  //   if(this.loginForm?.invalid || !this.isButtonDisabled){
  //     this.isButtonDisabled = true;
  //   }else{
  //     this.isButtonDisabled = false;
  //   }   
  //   return this.isButtonDisabled;
  // }

  reloadPage(): void {
    window.location.reload();
  }

}
