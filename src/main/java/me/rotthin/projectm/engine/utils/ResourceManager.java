package me.rotthin.projectm.engine.utils;

import me.rotthin.projectm.engine.rendering.Shader;
import me.rotthin.projectm.engine.rendering.SpriteSheet;
import me.rotthin.projectm.engine.rendering.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static final Map<String, Shader> shaders = new HashMap<>();
    private static final Map<String, Texture> textures = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    public static Shader getShader(String a_name){
        File _file = new File(a_name);

        if(shaders.containsKey(_file.getAbsolutePath())){
            return shaders.get(_file.getAbsolutePath());
        }else{
            Shader _shader = new Shader(a_name);
            _shader.compile();

            shaders.put(_file.getAbsolutePath(), _shader);

            return _shader;
        }
    }

    public static Texture getTexture(String a_name){
        File _file = new File(a_name);

        if(textures.containsKey(_file.getAbsolutePath())){
            return textures.get(_file.getAbsolutePath());
        }else{
            Texture _texture = new Texture();
            _texture.init(a_name);
            textures.put(_file.getAbsolutePath(), _texture);

            return _texture;
        }
    }

    public static void addSpriteSheet(String a_name, SpriteSheet a_spriteSheet){
        File _file = new File(a_name);

        if(!spriteSheets.containsKey(_file.getAbsolutePath())){
            spriteSheets.put(_file.getAbsolutePath(), a_spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String a_name){
        File _file = new File(a_name);

        assert spriteSheets.containsKey(_file.getAbsolutePath()) : "Error: Tried to access spritesheet that is not added to map.";
        return spriteSheets.getOrDefault(_file.getAbsolutePath(), null);
    }
}
