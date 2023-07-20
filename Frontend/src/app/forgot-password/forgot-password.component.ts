import { Component, ViewChild } from '@angular/core';
import { NgForm, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  @ViewChild("forgotPasswordForm", { static: false }) forgotPasswordForm!: NgForm;

  isSuccessful = false;
  isResetPasswordFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService){
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.forgotPasswordForm.controls["email"].addValidators([
        Validators.required,
        Validators.pattern("^[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}$")
      ]); 
      this.forgotPasswordForm.controls["email"].updateValueAndValidity;
    }, 0);    
  }

  ngOnInit(): void {   
  }

  onSubmit(){
    const requestData = {
      email: this.forgotPasswordForm.value.email,
    };
    console.log(this.forgotPasswordForm.value);
    this.authService.forgotPassword(requestData).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isResetPasswordFailed = false;
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isResetPasswordFailed = true;
      }
    })
  }

  // private isButtonDisabled = false;
  // resetPasswordButtonDisabled(): boolean {
  //   if(this.forgotPasswordForm?.invalid || !this.isButtonDisabled){
  //     this.isButtonDisabled = true;
  //   }else{
  //     this.isButtonDisabled = false;
  //   }   
  //   return this.isButtonDisabled;
  // }

}
