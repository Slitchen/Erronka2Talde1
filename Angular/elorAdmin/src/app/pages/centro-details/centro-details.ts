import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Consultas } from '../../services/consultas/consultas';
import { Centro } from '../../interface/centro';
import * as L from 'leaflet';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-centro-details',
  templateUrl: './centro-details.html',
  imports: [CommonModule, TranslateModule],
  styleUrl: './centro-details.css',
})

export class CentroDetails {
  /* INSTANCIA DE ACTIVATEDROUTE PARA LEER EL CODIGO DEL CENTRO DE LA URL*/
  private route = inject(ActivatedRoute);
  /* INSTANCIA DEL SERVICIO CONSULTAS */
  private consultas = inject(Consultas);

  /* SIGNAL QUE GUARDA EL CENTRO CON CODIGO DE CENTRO CONSEGUDO DE LA URL */
  centro = signal<Centro | null>(null);
  /* VARIABLE QUE GUARDA LA INSTANCIA DEL MAPA QUE SE CREA CON LEAFLET */
  private map: L.Map | null = null;

  /* SIGNAL QUE INDICA SI LAS COORDENADAS SON VALIDAS O NO */
  coordenadasInvalidas = signal(false);

  /* METODO QUE SE EJECUTA AL INICIALIZAR LA PAGINA */
  ngOnInit() {
    // VARIABLE QUE GUARDA EL CODIGO DE CENTRO CONSEGUIDO DE LA URL
    const ccen = Number(this.route.snapshot.paramMap.get('ccen'));

    // FILTRAR TODOS LOS CENTROS PARA ENCONTRAR EL QUE COINCIDA CON EL CODIGO DE CENTRO CONSEGUIDO DE LA URL
    this.consultas.getCentros().subscribe(res => {
      const centros = res.CENTROS; //ARRAY DE CENTROS
      const encontrado = centros.find(c => c.CCEN === ccen) || null; //CENTRO ENCONTRADO ENTRE EL ARRAY DE CENTROS A TRAVES DEL CODIGO DE CENTRO
      this.centro.set(encontrado); //GUARDAR EN EL SIGNAL CENTRO EL CENTRO ENCONTRADO

      /* SI SE HA ENCONTRADO CENTRO CON EL CODIGO DE CENTRO INICIALIZAR EL MAPA */
      if (encontrado) {
        this.initMap(encontrado);
      }
    });
  }

  /* METODO PARA INICIALIZAR EL MAPA */
private initMap(centro: Centro) {
  if (this.map) return;

  const lat = Number(centro.LATITUD);
  const lon = Number(centro.LONGITUD);

  if (lat === 0 && lon === 0) {
    this.coordenadasInvalidas.set(true);
    return;
  }

  const iconDefault = L.icon({
    iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41]
  });
  L.Marker.prototype.options.icon = iconDefault;

  setTimeout(() => {
    this.map = L.map('map').setView([lon, lat], 16);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    L.marker([lon, lat])
      .addTo(this.map);

    this.map.invalidateSize();
  }, 0);
}

}
