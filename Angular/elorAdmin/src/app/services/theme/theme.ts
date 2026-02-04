import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Theme {
  private darkModeKey = 'darkMode';

  constructor(){
    if (this.isDarkMode()) {
      document.body.classList.add('dark-mode');
    }
  }
  
  enableDarkMode() {
    document.body.classList.add('dark-mode');
    sessionStorage.setItem(this.darkModeKey, 'true');
  }

  enableLightMode() {
    document.body.classList.remove('dark-mode');
    sessionStorage.setItem(this.darkModeKey, 'false');
  }

  toggleTheme() {
    if (this.isDarkMode()) {
      this.enableLightMode();
    } else {
      this.enableDarkMode();
    }
  }

  isDarkMode(): boolean {
    return sessionStorage.getItem(this.darkModeKey) === 'true';
  }
}
