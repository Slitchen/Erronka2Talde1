using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Json;
using System.Text;
using System.Threading.Tasks;
using ElorMAUI.Models;

namespace ElorMAUI.Services
{
    public class AirQualityService
    {
        private readonly HttpClient _http;
        private readonly string apiKey = "643fbd1dcdb693f9eb8d41e9c66ad39b";

        public AirQualityService(HttpClient http)
        {
            _http = http;
        }

        public async Task<AirQualityResponse?> GetAirQualityAsync(double lat, double lon)
        {
            //var url = $"https://api.openweathermap.org/data/2.5/air_pollution" +$"?lat={lon}&lon={lat}&appid={apiKey}";
            //string url = $"https://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={apiKey}";
            string url = $"http://api.openweathermap.org/data/2.5/air_pollution?lat={lat}&lon={lon}&appid={apiKey}";

            return await _http.GetFromJsonAsync<AirQualityResponse>(url);
        }
    }
}
