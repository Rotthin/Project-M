package net.pixeltree.project_m.engine;

public abstract class Component {
    public GameObject gameObject = null;

    public void start(){ }

    public abstract void update(float a_dt);
}
