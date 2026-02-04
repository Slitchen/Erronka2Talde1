import { Component } from '@angular/core';
import { Theme } from '../../services/theme/theme';

@Component({
  selector: 'app-theme-selector',
  imports: [],
  templateUrl: './theme-selector.html',
  styleUrl: './theme-selector.css',
})
export class ThemeSelector {
  constructor(private themeService: Theme) {}  

  //GETTER PARA SABER SI ESTA EL MODO OSCURO ACTIVADO
  get isDarkMode(): boolean {
    return this.themeService.isDarkMode();
  }

  //METODO PARA CAMBIAR EL TEMA
  toggleTheme() {
    this.themeService.toggleTheme();
  }
}
