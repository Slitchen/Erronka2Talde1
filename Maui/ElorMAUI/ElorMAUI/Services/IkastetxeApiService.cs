using ElorMAUI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Json;
using System.Text;
using System.Threading.Tasks;

namespace ElorMAUI.ApiService
{
    public class IkastetxeApiService
    {
        private readonly HttpClient _http;

        public IkastetxeApiService(HttpClient http)
        {
            _http = http;
        }

        
        public async Task<List<Ikastetxea>> GetIkastetxeakAsync()
        {
            return await _http.GetFromJsonAsync<List<Ikastetxea>>(
                "https://link_del_api"
                ) ?? new();
        }
        
    }
}
