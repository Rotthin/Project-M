package me.rotthin.projectm.engine.rendering;

import org.joml.Vector2f;

public class Sprite {
    private float width, height;

    private Texture texture;
    private Vector2f[] uvs = {
        new Vector2f(1, 1),
        new Vector2f(1, 0),
        new Vector2f(0, 0),
        new Vector2f(0, 1)
    };

    public Sprite(){
        init(null, uvs);
    }

    public Sprite(Texture a_tex){
        init(a_tex, uvs);
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

    public void setTexture(Texture a_tex){
        texture = a_tex;
    }

    public void setUvs(Vector2f[] a_uvs){
        uvs = a_uvs;
    }

    public int getTextureID(){
        return texture == null ? -1 : texture.getID();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float a_w) {
        width = a_w;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float a_h) {
        height = a_h;
    }
}
