using System.Net.Http.Json;
using ElorMAUI.Models;

namespace ElorMAUI.Services
{
    public class WeatherService
    {
        //Json = https://api.openweathermap.org/data/2.5/forecast?lat=43.2630&lon=-2.9350&appid=643fbd1dcdb693f9eb8d41e9c66ad39b

        private readonly HttpClient _http;
        private readonly string apiKey = "643fbd1dcdb693f9eb8d41e9c66ad39b";

        public WeatherService()
        {
            _http = new HttpClient();
        }

        public async Task<WeatherResponse?> GetCurrentWeather(double lat, double lon)
        {
            string url =
                $"https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&units=metric&lang=es&appid={apiKey}";

            return await _http.GetFromJsonAsync<WeatherResponse>(url);
        }

        public async Task<ForecastResponse?> GetForecast(double lat, double lon)
        {
            string url =
                $"https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&units=metric&lang=es&appid={apiKey}";

            return await _http.GetFromJsonAsync<ForecastResponse>(url);
        }
    }
}
