package net.pixeltree.project_m.engine.utils;

import net.pixeltree.project_m.engine.renderer.Shader;
import net.pixeltree.project_m.engine.renderer.SpriteSheet;
import net.pixeltree.project_m.engine.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();

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
            Texture _texture = new Texture(a_name);
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

        if(!spriteSheets.containsKey(_file.getAbsolutePath())){
            assert false: "Error: Tried to access spritesheet that is not added to map.";
        }

        return spriteSheets.getOrDefault(_file.getAbsolutePath(), null);
    }
}
