using ElorMAUI.ApiService;
using Microsoft.Extensions.Logging;
using ElorMAUI.Services;

namespace ElorMAUI
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();
            builder
                .UseMauiApp<App>()
                .UseMauiMaps()
                .ConfigureFonts(fonts =>
                {
                    fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                });

            builder.Services.AddMauiBlazorWebView();
            builder.Services.AddSingleton<IkastetxeService.IkastetxeService>();
            /*builder.Services.AddSingleton(sp =>
            {
                var httpClient = new HttpClient
                {
                    BaseAddress = new Uri("https://api_link")
                };
                return httpClient;
            });*/
            builder.Services.AddSingleton<IkastetxeApiService>();

            builder.Services.AddSingleton<WeatherService>();

            builder.Services.AddSingleton<AirQualityService>();

            builder.Services.AddSingleton(sp =>
                new HttpClient { BaseAddress = new Uri("https://http://10.5.104.131:1313/") }
            );

            builder.Services.AddSingleton<LoginService>();
            builder.Services.AddSingleton<SessionService>();

            builder.Services.AddSingleton<NavMenuService>();


#if DEBUG
            builder.Services.AddBlazorWebViewDeveloperTools();
    		builder.Logging.AddDebug();

#endif

            return builder.Build();
        }
    }
}
