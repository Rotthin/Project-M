package net.pixeltree.project_m.engine.math;

public class Color {
    public float r;
    public float g;
    public float b;
    public float a;

    public Color(float a_r, float a_g, float a_b, float a_a){
        r = a_r;
        g = a_g;
        b = a_b;
        a = a_a;
    }

    public static Color white(){ return new Color(1.0f, 1.0f, 1.0f, 1.0f); }
    public static Color black(){ return new Color(0.0f, 0.0f, 0.0f, 0.0f); }
    public static Color red(){ return new Color(1.0f, 0.0f, 0.0f, 1.0f); }
    public static Color green(){ return new Color(0.0f, 1.0f, 0.0f, 1.0f); }
    public static Color blue(){ return new Color(0.0f, 0.0f, 1.0f, 1.0f); }
}
