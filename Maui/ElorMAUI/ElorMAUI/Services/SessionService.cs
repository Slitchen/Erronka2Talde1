using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ElorMAUI.Services
{
    public class SessionService
    {
        public bool IsLogged { get; private set; } = false;
        public string? Username { get; private set; }

        public event Action? OnChange;

        public void Login(string username)
        {
            IsLogged = true;
            Username = username;
            OnChange?.Invoke();
        }

        public void Logout()
        {
            IsLogged = false;
            Username = null;
            OnChange?.Invoke();
        }
    }

}
