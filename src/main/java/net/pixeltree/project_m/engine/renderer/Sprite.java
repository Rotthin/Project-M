package net.pixeltree.project_m.engine.renderer;

import org.joml.Vector2f;

public class Sprite {
    private Texture texture;
    private Vector2f[] uvs;

    public Sprite(Texture a_tex){
        init(a_tex, new Vector2f[] {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)});
    }

    public Sprite(Texture a_tex, Vector2f[] a_uvs){
        init(a_tex, a_uvs);
    }

    private void init(Texture a_tex, Vector2f[] a_uvs){
        texture = a_tex;
        uvs = a_uvs;
    }

    public Texture getTexture(){
        return texture;
    }

    public Vector2f[] getUvs(){
        return uvs;
    }
}
