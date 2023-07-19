import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { NgForm, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, AfterViewInit{
  @ViewChild("registerForm", { static: false }) registerForm!: NgForm;

  isSuccessful = false;
  isRegisterFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService){
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.registerForm.controls["email"].addValidators([
        Validators.required,
        Validators.pattern("^[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}$")
      ]); 
      this.registerForm.controls["email"].updateValueAndValidity;

      this.registerForm.controls["password"].addValidators([
        Validators.required,
        Validators.pattern("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[ !\"#$%&'()*+,-./:;<=>?@\\[\\\\\\]^_`{|}~]).{8,25}$")
      ]); 
      this.registerForm.controls["password"].updateValueAndValidity;
      
      this.registerForm.controls["contactNumber"].addValidators([
        Validators.required,
        Validators.pattern("^(0[3|5|7|8|9])+([0-9]{8})$")
      ]); 
      this.registerForm.controls["contactNumber"].updateValueAndValidity;
    }, 0);    
  }

  ngOnInit(): void {   
  }

  onSubmit(){
    const requestData = {
      name: this.registerForm.value.name,
      email: this.registerForm.value.email,
      password: this.registerForm.value.password,
      contactNumber: this.registerForm.value.contactNumber
    };
    console.log(this.registerForm.value);
    this.authService.register(requestData).subscribe({
      next: data => {
        console.log(data);
        this.isSuccessful = true;
        this.isRegisterFailed = false;
      },
      error: err => {
        this.errorMessage = err.error.message;
        this.isRegisterFailed = true;
      }
    })
  }

  private isButtonDisabled = false;
  registerButtonDisabled(): boolean {
    if(this.registerForm?.invalid || !this.isButtonDisabled){
      this.isButtonDisabled = true;
    }else{
      this.isButtonDisabled = false;
    }   
    return this.isButtonDisabled;
  }

}
