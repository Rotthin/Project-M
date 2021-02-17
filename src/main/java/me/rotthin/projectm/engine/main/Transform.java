package me.rotthin.projectm.engine.main;

import me.rotthin.projectm.engine.editor.imgui.ImGuiUtils;
import org.joml.Vector2f;

public class Transform {
    public Vector2f position;
    public Vector2f scale;

    public Transform(){
        init(new Vector2f(), new Vector2f(1,1));
    }

    public Transform(Vector2f a_pos){
        init(a_pos, new Vector2f());
    }

    public Transform(Vector2f a_pos, Vector2f a_scale){
        init(a_pos, a_scale);
    }

    public Transform copy(){
        return new Transform(new Vector2f(position), new Vector2f(scale));
    }

    public void copy(Transform a_to){
        a_to.position.set(position);
        a_to.scale.set(scale);
    }

    public void imgui(){
        ImGuiUtils.showFields(this, true);
    }

    @Override
    public boolean equals(Object a_other){
        if(!(a_other instanceof Transform)) return false;

        Transform _t = (Transform)a_other;
        return _t.position.equals(position) && _t.scale.equals(scale);
    }

    public void init(Vector2f a_pos, Vector2f a_scale){
        position = a_pos;
        scale = a_scale;
    }
}
