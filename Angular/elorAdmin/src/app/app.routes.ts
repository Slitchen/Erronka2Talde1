import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { authGuardGuard } from '../app/guards/auth-guard-guard';
import { Role } from './models/Role';
import { AdminHome } from './pages/admin-home/admin-home';
import { IkasleHome } from './pages/ikasle-home/ikasle-home';

export const routes: Routes = [
  
  { path: 'login', component: Login },
  { path: 'god', loadComponent: () => import('./pages/god-home/god-home').then(c => c.GodHome), canActivate: [authGuardGuard], data: { roles: [Role.GOD] } },
  { path: 'administrador', component: AdminHome, canActivate: [authGuardGuard], data: { roles: [Role.ADMINISTRADOR] } },
  { path: 'profesor', loadComponent: () => import('./pages/irakasle-home/irakasle-home').then(c => c.IrakasleHome), canActivate: [authGuardGuard], data: { roles: [Role.PROFESOR] }},
  { path: 'alumno', component: IkasleHome, canActivate: [authGuardGuard], data: { roles: [Role.ALUMNO] } },
  { path: 'centro/:ccen', loadComponent: () => import('./pages/centro-details/centro-details').then(c => c.CentroDetails), canActivate: [authGuardGuard], data: { roles: [Role.PROFESOR, Role.ALUMNO, Role.ADMINISTRADOR] } },

  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];