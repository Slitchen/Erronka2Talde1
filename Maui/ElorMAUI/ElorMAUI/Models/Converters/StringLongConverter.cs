using System;
using System.Globalization;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace ElorMAUI.Models.Converters
{
    public class StringLongConverter : JsonConverter<long?>
    {
        public override long? Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            switch (reader.TokenType)
            {
                case JsonTokenType.Number:
                    return reader.GetInt64();

                case JsonTokenType.String:
                    var value = reader.GetString();
                    if (string.IsNullOrWhiteSpace(value))
                        return null;

                    if (long.TryParse(value, NumberStyles.Any, CultureInfo.InvariantCulture, out var result))
                        return result;

                    return null; // Si no es un número válido, devuelve null

                case JsonTokenType.Null:
                    return null;

                default:
                    return null;
            }
        }

        public override void Write(Utf8JsonWriter writer, long? value, JsonSerializerOptions options)
        {
            if (value.HasValue)
                writer.WriteNumberValue(value.Value);
            else
                writer.WriteNullValue();
        }
    }
}
