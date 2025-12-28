package backend;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter
        implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext ctx) {
        return new JsonPrimitive(src.toString()); // ISO-8601
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx)
            throws JsonParseException {
        return LocalDateTime.parse(json.getAsString());
    }
}
