import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { Role } from '../models/Role';

export const authGuardGuard: CanActivateFn = (route, state): boolean | UrlTree => {
  //SERVICIO DE NAVEGACION
  const router = inject(Router);

  //OBTENEMOS EL USUARIO GUARDADO EN SESSIONSTORAGE
  const saved = sessionStorage.getItem('user');
  const user = saved ? JSON.parse(saved) : null;

  //SI NO HAY USUARIO, REDIRIGIMOS AL LOGIN
  if (!user) {
    return router.createUrlTree(['/login']);
  }

  //VERIFICAMOS SI EL ROL DEL USUARIO ESTA PERMITIDO EN LA RUTA
  const allowedRoles: Role[] = route.data?.['roles'] ?? [];

  //SI HAY ROLES PERMITIDOS Y EL ROL DEL USUARIO NO ESTA INCLUIDO, REDIRIGIMOS AL LOGIN
  if (allowedRoles.length > 0 && !allowedRoles.includes(user.tipo_id as Role)) {
    return router.createUrlTree(['/login']);
  }

  //SI TODO ESTA BIEN, PERMITIMOS EL ACCESO A LA RUTA
  return true;
};

