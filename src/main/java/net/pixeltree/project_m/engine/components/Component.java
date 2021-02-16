package net.pixeltree.project_m.engine.components;

import net.pixeltree.project_m.engine.GameObject;

public abstract class Component {
    public transient GameObject gameObject = null;

    public void start(){}
    public void update(float a_dt){}
    public void imgui(){}
}
