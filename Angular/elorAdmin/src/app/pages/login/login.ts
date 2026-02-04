import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Auth } from '../../services/auth/auth';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, TranslateModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login {

  /* FORMULARIO */
  loginForm: FormGroup;

  /* MENSAJE DE ERROR PARA EL FORMULARIO */
  errorMessage: string = '';

  /* CONSTRUCTOR */
  constructor(
    private fb: FormBuilder,
    private authService: Auth,
    private router: Router,
  ) {
    /* FORMULARIO + VALIDACION DE REQUERIMIENTO */
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

    /* 
    METODO PARA RECOGER LOS VALORES INTRODUCIDOS EN EL FORMULARIO Y 
    COMPARARLOS CON LOS DATOS RECOGIDOS DE LA BASE DE DATOS. 
    SI EXITE EL USUARIO NAVEGAR A LA PAGINA HOME QUE CORRESPONDA SEGUN SU TIPO DE USUARIO
    */
  login(): void {
    if (this.loginForm.invalid) return;

    const { username, password } = this.loginForm.value;

    this.authService.login(username, password).subscribe({
      next: (user) => {
        this.authService.setUser(user);
        
        switch (user.tipo_id) {
          case 1: this.router.navigate([`/god`]); break;
          case 2: this.router.navigate([`/administrador`]); break;
          case 3: this.router.navigate([`/profesor`]); break;
          case 4: this.router.navigate([`/alumno`]); break;
        }
      },
      error: () => {
        this.errorMessage = 'LOGIN.ERROR_AUTH';
      }
    });
  }

}
