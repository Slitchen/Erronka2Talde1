using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ElorMAUI.Models
{
    public class WeatherResponse
    {
        public MainInfo? Main { get; set; }
        public List<WeatherInfo>? Weather { get; set; }
    }

    public class MainInfo
    {
        public double Temp { get; set; }
    }

    public class WeatherInfo
    {
        public string Description { get; set; }
    }
    

}
