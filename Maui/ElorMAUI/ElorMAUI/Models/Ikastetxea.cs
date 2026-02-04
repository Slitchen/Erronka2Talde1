using ElorMAUI.Models.Converters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace ElorMAUI.Models
{
    public class Ikastetxea
    {
        [JsonPropertyName("CCEN")]
        [JsonConverter(typeof(StringLongConverter))]
        public long? Id { get; set; }

        [JsonPropertyName("NOME")]
        public string? Izena { get; set; }

        [JsonPropertyName("PAGINA")]
        public string? Orrialdea { get; set; }

        [JsonPropertyName("DTITUC")]
        public string? Mota { get; set; }

        [JsonPropertyName("DTERRE")]
        public string? Lurraldea { get; set; }

        [JsonPropertyName("DMUNIC")]
        public string? Udalerria { get; set; }
        
        [JsonPropertyName("DOMI")]
        public string? Direccion { get; set; }

        [JsonPropertyName("EMAIL")]
        public string? Email { get; set; }

        [JsonPropertyName("TEL1")]
        [JsonConverter(typeof(StringLongConverter))]
        public long? Telefono { get; set; }

        [JsonPropertyName("CPOS")]
        [JsonConverter(typeof(StringLongConverter))]
        public long? CodigoPostal { get; set; }

        [JsonPropertyName("TFAX")]
        [JsonConverter(typeof(StringLongConverter))]
        public long? NumFax { get; set; }

        
        [JsonPropertyName("LATITUD")]
        public double? Latitud { get; set; }
        

        [JsonPropertyName("LONGITUD")]
        public double? Longitud { get; set; }
    }

    public class Root
    {
        [JsonPropertyName("CENTROS")]
        public List<Ikastetxea>? Centros { get; set; }
    }
}
