import { Component } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Auth } from './services/auth/auth';
import { LanguageSelector } from './pages/language-selector/language-selector';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ThemeSelector } from './pages/theme-selector/theme-selector';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, LanguageSelector, TranslateModule, ThemeSelector],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App {
  isLoggedIn = false;
  userRole = '';
  isLoginRoute = false;

  constructor(private authService: Auth, private router: Router, private translate: TranslateService) {
    //DETECTAR CANBIO DE USUARIO (LOGIN / LOGOUT)
    this.authService.onUserChange().subscribe(user => {
      this.isLoggedIn = !!user;
      this.userRole = this.authService.getRoleText(user?.tipo_id);
    });

    //DETECTAR CAMBIO A RUTA LOGIN
    this.router.events.subscribe(() => { 
      this.isLoginRoute = this.router.url === '/login'; 
    });

    //IDIOMAS DISPONIBLES
    this.translate.addLangs(['es', 'eu', 'en']);
    //IDIOMA POR DEFECTO
    this.translate.setDefaultLang('es');

    //RECUPERAR IDIOMA GUARDADO O USAR EL DEL NAVEGADOR
    const browserLang = sessionStorage.getItem('lang') || this.translate.getBrowserLang();
    this.translate.use(browserLang?.match(/es|eu|en/) ? browserLang : 'es');
  }

  //METODO LOGOUT
  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  //METODO NAVEGAR A HOME SEGUN ROL
  onLogoClick() {

    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    switch (this.userRole) {
      case 'GOD': this.router.navigate([`/god`]); break;
      case 'ADMINISTRADOR': this.router.navigate([`/administrador`]); break;
      case 'PROFESOR': this.router.navigate([`/profesor`]); break;
      case 'ALUMNO': this.router.navigate([`/alumno`]); break;
    }
  }
}
