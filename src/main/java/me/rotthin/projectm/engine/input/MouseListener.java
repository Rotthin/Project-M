package me.rotthin.projectm.engine.input;

import me.rotthin.projectm.engine.renderer.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private final boolean[] mouseButtonDown = new boolean[3];
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
        instance.isDragging = isButtonDown(0) || isButtonDown(1) || isButtonDown(2);
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

    public static float getOrthoY(){
        float _currentY = Window.getHeight() - getY();

        // Get the -1.0 - 1.0 mouse x pos
        _currentY = (_currentY / (float)Window.getHeight() ) * 2.0f -1.0f;

        Vector4f _tmp = new Vector4f(0, _currentY, 0, 1);

        _tmp.mul(Window.getCurrentScene().getCamera().getInvProjectionMatrix()).mul(Window.getCurrentScene().getCamera().getInvViewMatrix());

        return _tmp.y;
    }

    public static float getOrthoX(){
        float _currentX = getX();
        // Get the -1.0 - 1.0 mouse x pos
        _currentX = (_currentX / (float)Window.getWidth()) * 2.0f -1.0f;

        Vector4f _tmp = new Vector4f(_currentX, 0, 0, 1);

        _tmp.mul(Window.getCurrentScene().getCamera().getInvProjectionMatrix()).mul(Window.getCurrentScene().getCamera().getInvViewMatrix());

        return _tmp.x;
    }

    public static Vector2f getOrtho(){
        float _currentX = getX();
        // Get the -1.0 - 1.0 mouse x pos
        _currentX = (_currentX / (float)Window.getWidth()) * 2.0f -1.0f;

        float _currentY = Window.getHeight() - getY();
        // Get the -1.0 - 1.0 mouse y pos
        _currentY = (_currentY / (float)Window.getHeight()) * 2.0f -1.0f;

        Vector4f _tmp = new Vector4f(_currentX, _currentY, 0, 1);

        _tmp.mul(Window.getCurrentScene().getCamera().getInvProjectionMatrix()).mul(Window.getCurrentScene().getCamera().getInvViewMatrix());

        return new Vector2f(_tmp.x, _tmp.y);
    }

    public static boolean isButtonDown(int a_button){
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
