package net.pixeltree.project_m.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonDown[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
        instance = this;
    }

    public static MouseListener getInstance(){
        checkInstanceNull();

        return instance;
    }

    public static void mousePosCallback(long a_window, double a_xPos, double a_yPos){
        checkInstanceNull();

        instance.lastX = instance.xPos;
        instance.lastY = instance.yPos;
        instance.xPos = a_xPos;
        instance.yPos = a_yPos;
        instance.isDragging = mouseButtonDown(0) || mouseButtonDown(1) || mouseButtonDown(2);
    }

    public static void mouseButtonCallback(long a_window, int a_button, int a_action, int a_mods){
        checkInstanceNull();

        if(a_action == GLFW_PRESS){
            if(a_button < instance.mouseButtonDown.length) {
                instance.mouseButtonDown[a_button]=true;
            }
        }else if(a_action == GLFW_RELEASE){
            if(a_button < instance.mouseButtonDown.length){
                instance.mouseButtonDown[a_button]=false;
                instance.isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long a_window, double a_xOff, double a_yOff){
        checkInstanceNull();

        instance.scrollX = a_xOff;
        instance.scrollY = a_yOff;
    }

    public static void reset(){
        checkInstanceNull();

        instance.scrollX = 0;
        instance.scrollY = 0;
        instance.lastX = instance.xPos;
        instance.lastY = instance.yPos;
    }

    public static float getX(){
        checkInstanceNull();

        return (float)instance.xPos;
    }

    public static float getY(){
        checkInstanceNull();

        return (float)instance.yPos;
    }

    public static float getDx(){
        checkInstanceNull();

        return (float)(instance.lastX - instance.xPos);
    }

    public static float getDy(){
        checkInstanceNull();

        return (float)(instance.lastY - instance.yPos);
    }

    public static float getScrollX(){
        checkInstanceNull();

        return (float)instance.scrollX;
    }

    public static float getScrollY(){
        checkInstanceNull();

        return (float)instance.scrollY;
    }

    public static boolean isDragging(){
        checkInstanceNull();

        return instance.isDragging;
    }

    public static boolean mouseButtonDown(int a_button){
        checkInstanceNull();

        if(a_button < instance.mouseButtonDown.length){
            return instance.mouseButtonDown[a_button];
        }

        return false;
    }

    private static void checkInstanceNull(){
        if(instance == null) instance = new MouseListener();
    }
}
