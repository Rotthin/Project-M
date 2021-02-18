package me.rotthin.projectm.engine.components;

import imgui.ImGui;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.editor.gui.imgui.ImGuiUtils;

public abstract class Component {
    private static int s_idCounter = 0;
    private int uid=-1;

    public transient GameObject gameObject = null;

    public void start(){}
    public void update(float a_dt){}
    public void imgui(boolean a_header){
        ImGuiUtils.showFields(this, a_header);
        if(ImGui.button("Remove component")){
            gameObject.removeComponent(this);
        }
    }

    public void genID(){
        if(uid == -1){
            uid = s_idCounter++;
        }
    }

    public int getUid(){
        return uid;
    }

    public static void init(int a_maxId){
        s_idCounter = a_maxId;
    }
}
