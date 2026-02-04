using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ElorMAUI.Services
{
    public class NavMenuService
    {
        // Lista de enlaces dinámicos
        private readonly List<NavItem> _items = new();

        public IReadOnlyList<NavItem> Items => _items.AsReadOnly();

        public event Action? OnChange;

        public void AddNavItem(string title, string href)
        {
            if (!_items.Any(x => x.Href == href))
            {
                _items.Add(new NavItem { Title = title, Href = href });
                NotifyStateChanged();
            }
        }

        public void RemoveNavItemsStartingWith(string prefix)
        {
            var itemsToRemove = _items.Where(x => x.Href.StartsWith(prefix)).ToList();
            if (itemsToRemove.Any())
            {
                foreach (var item in itemsToRemove)
                {
                    _items.Remove(item);
                }
                NotifyStateChanged();
            }
        }


        private void NotifyStateChanged() => OnChange?.Invoke();
    }

    public class NavItem
    {
        public string Title { get; set; } = "";
        public string Href { get; set; } = "";
        public Dictionary<string, object>? Parameters { get; set; } // opcional
    }

}
