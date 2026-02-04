import { Component, OnInit } from '@angular/core';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-language-selector',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './language-selector.html',
  styleUrl: './language-selector.css'
})
export class LanguageSelector implements OnInit {
  // IDIOMA ACTUAL, POR DEFECTO
  currentLang: string = 'es';

  constructor(private translate: TranslateService) {}

  // INICIALIZAR EL IDIOMA ACTUAL
  ngOnInit() {
    this.currentLang = this.translate.currentLang || 'es';
  }

  // CAMBIAR EL IDIOMA
  changeLang(lang: string) {
    this.translate.use(lang);
    this.currentLang = lang;
    // GUARDAR EL IDIOMA EN SESSIONSTORAGE, AL RECARGAR LA PÁGINA SE MANTIENE EL IDIOMA
    sessionStorage.setItem('lang', lang);
  }
}