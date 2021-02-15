package net.pixeltree.project_m.engine.components;

import net.pixeltree.project_m.engine.Component;
import net.pixeltree.project_m.engine.Transform;
import net.pixeltree.project_m.engine.renderer.Sprite;
import net.pixeltree.project_m.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty;

    public SpriteRenderer(Vector4f a_color){
        init(a_color, null);
    }

    public SpriteRenderer(Sprite a_sprite){
        init(new Vector4f(1, 1, 1, 1), a_sprite);
    }

    private void init(Vector4f a_color, Sprite a_sprite){
        color = a_color;
        sprite = a_sprite;
        isDirty = true;
    }

    @Override
    public void start(){
        lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float a_dt) {
        if(!lastTransform.equals(gameObject.transform)){
            gameObject.transform.copy(lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColor(){
        return color;
    }

    public Texture getTexture(){
        if(sprite != null){
            return sprite.getTexture();
        }

        return null;
    }

    public Vector2f[] getUvs(){
        if(sprite != null) {
            return sprite.getUvs();
        }

        return null;
    }

    public void setSprite(Sprite a_sprite){
        sprite = a_sprite;
        isDirty = true;
    }

    public void setColor(Vector4f a_color){
        if(!color.equals(a_color)){
            color.set(a_color);
            isDirty = true;
        }
    }

    public boolean getIsDirty(){
        return isDirty;
    }

    public void setIsDirty(boolean a_value){
        isDirty = a_value;
    }
}
