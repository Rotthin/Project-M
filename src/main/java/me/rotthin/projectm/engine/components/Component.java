package me.rotthin.projectm.engine.components;

import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.editor.imgui.ImGuiUtils;

public abstract class Component {
    public transient GameObject gameObject = null;

    public void start(){}
    public void update(float a_dt){}
    public void imgui(boolean a_header){
        ImGuiUtils.showFields(this, a_header);
    }
}
