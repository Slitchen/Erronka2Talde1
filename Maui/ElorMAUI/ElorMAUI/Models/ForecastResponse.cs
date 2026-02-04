using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ElorMAUI.Models
{
    public class ForecastResponse
    {
        public List<ForecastItem>? List { get; set; }
    }

    public class ForecastItem
    {
        public string Dt_txt { get; set; }
        public MainInfo Main { get; set; }
        public List<WeatherInfo> Weather { get; set; }


        private static readonly Dictionary<int, string> DiasRelativos = new()
        {
            {0, "Hoy"},
            {1, "Mañana"},
        };

        public string Dt_inteligente
        {
            get
            {
                if(DateTime.TryParseExact(Dt_txt, "yyyy-MM-dd HH:mm:ss",
                    CultureInfo.InvariantCulture,
                    DateTimeStyles.None,
                    out var fechaDt))
                {
                    int diffDays = (fechaDt.Date - DateTime.Today).Days;

                    if (DiasRelativos.TryGetValue(diffDays, out var descripcion))
                        return $"{descripcion} {fechaDt:HH:mm}";

                    return fechaDt.ToString("dd/MM/yyyy HH:mm");
                }

                return Dt_txt;
            }
        }
    }

}
