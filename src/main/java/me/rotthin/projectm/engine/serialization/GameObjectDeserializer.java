package me.rotthin.projectm.engine.serialization;

import com.google.gson.*;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.main.Transform;
import me.rotthin.projectm.engine.components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject _json = json.getAsJsonObject();
        String _name = _json.get("name").getAsString();
        JsonArray _componenets = _json.getAsJsonArray("components");
        Transform _transf = context.deserialize(_json.get("transform"), Transform.class);
        int _zIndex = context.deserialize(_json.get("zIndex"), int.class);

        GameObject _go = new GameObject(_name, _transf, _zIndex);

        for(JsonElement e : _componenets){
            Component _c = context.deserialize(e, Component.class);
            _go.addComponent(_c);
        }

        return _go;
    }
}
