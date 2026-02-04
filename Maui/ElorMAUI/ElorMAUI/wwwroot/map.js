delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: 'https://unpkg.com/leaflet/dist/images/marker-icon-2x.png',
    iconUrl: 'https://unpkg.com/leaflet/dist/images/marker-icon.png',
    shadowUrl: 'https://unpkg.com/leaflet/dist/images/marker-shadow.png'
});

window.initMap = function (lon, lat, id, dotNetHelper) {
    var map = L.map('map').setView([lon, lat], 15);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19
    }).addTo(map);

    var marker = L.marker([lon, lat]).addTo(map);

    marker.on('click', function () {
        dotNetHelper.invokeMethodAsync("GoToWeather", id);
    });
}

