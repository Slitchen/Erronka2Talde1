using ElorMAUI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Json;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Threading.Tasks;
using static System.Net.WebRequestMethods;

namespace ElorMAUI.Services
{
    public class LoginService
    {
        private readonly HttpClient _http;
        private readonly SessionService _session;

        
        public LoginService(HttpClient http, SessionService session)
        {
            _http = http;
            _session = session;
        }
        

        /*
        public LoginService(HttpClient http)
        {
            _http = http;
        }
        */

        public async Task<bool> Login(User user)
        {
            string url = $"http://10.5.104.131:1313/login/{user.username}/{user.password}";

            try
            {
                // Intentar leer el JSON como ApiUser
                User? apiUser = await _http.GetFromJsonAsync<User>(url);

                // Si apiUser es null → login incorrecto
                if (apiUser == null)
                    return false;

                bool ok = apiUser.username == user.username && apiUser.password == user.password;

                // Validar username y password
                //return apiUser.username == user.username && apiUser.password == user.password;

                if (ok)
                {
                    _session.Login(apiUser.username);
                }

                return ok;
                

            }
            catch
            {
                // Si la API devuelve un texto de error → excepción → login incorrecto
                return false;

            }

        }

        
        public void Logout()
        {
            _session.Logout();
        }
        
    }
}
