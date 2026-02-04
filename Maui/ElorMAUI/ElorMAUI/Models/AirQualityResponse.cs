using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace ElorMAUI.Models
{
    public class AirQualityResponse
    {
        [JsonPropertyName("list")]
        public List<AirQualityItem> List { get; set; }
    }

    public class AirQualityItem
    {
        [JsonPropertyName("main")]
        public AirQualityMain Main { get; set; }
    }

    public class AirQualityMain
    {
        [JsonPropertyName("aqi")]
        public int Aqi { get; set; }

        public string AqiDescription => Aqi switch
        {
            1 => "Buena",
            2 => "Moderada",
            3 => "Pobre",
            4 => "Mala",
            5 => "Muy mala",
            _ => "Desconocida"
        };
    }
}
