package me.rotthin.projectm.engine.serialization;

import com.google.gson.*;
import me.rotthin.projectm.engine.components.Component;

import java.lang.reflect.Type;

public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject _json = json.getAsJsonObject();
        String _type = _json.get("type").getAsString();
        JsonElement _element = _json.get("properties");

        try{
            return context.deserialize(_element, Class.forName(_type));
        }catch(ClassNotFoundException e){
            throw new JsonParseException("Unknown element type: " + _type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject _res = new JsonObject();
        _res.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        _res.add("properties", context.serialize(src, src.getClass()));
        return _res;
    }
}
