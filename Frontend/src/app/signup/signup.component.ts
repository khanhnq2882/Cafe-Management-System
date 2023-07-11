import { AfterViewInit, Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit, AfterViewInit{

  signUpForm: FormGroup = new FormGroup({
    name: new FormControl(''),  
    email: new FormControl(''),
    password: new FormControl(''),
    contactNumber: new FormControl('')
  });

  constructor(private userService: UserService, private formBuilder: FormBuilder){
  }

  ngAfterViewInit(): void { 
  } 

  ngOnInit(): void {   
  }

  public signUp(){
    if (this.signUpForm.valid) {
      console.log(this.signUpForm.value);
      this.signUpForm.value.status = 'false';
      this.signUpForm.value.role = 'user';
      console.log(this.signUpForm.value);
      const requestData = this.signUpForm.value;
      this.userService.signUp(requestData)
        .subscribe(
          (response) => {
            console.log('Đăng ký thành công');
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }

}
