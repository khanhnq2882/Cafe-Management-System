import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit, AfterViewInit{
  @ViewChild('changePasswordForm', { static: false }) changePasswordForm!: NgForm;

  isSuccessful = false;
  isRegisterFailed = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router){}

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }

  onSubmit() {
    const requestData = {
      currentPassword: this.changePasswordForm.value.currentPassword,
      newPassword: this.changePasswordForm.value.currentPassword,
      confirmPassword: this.changePasswordForm.value.currentPassword,
    }
    console.log(requestData);
    this.authService.changePassword(requestData).subscribe({
      next: data => {
        this.isSuccessful = true;
        this.isRegisterFailed = false;
        alert('Change password successfully.')
      },
      error: err => {
        this.isSuccessful = true;
        this.isRegisterFailed = false;
        this.errorMessage = err.error.message;
        alert(this.errorMessage)
      },
    })

  }

}
