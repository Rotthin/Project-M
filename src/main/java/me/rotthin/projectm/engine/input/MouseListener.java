package me.rotthin.projectm.engine.input;

import me.rotthin.projectm.engine.main.Camera;
import me.rotthin.projectm.engine.rendering.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public final class MouseListener {
    private static double scrollX, scrollY;
    private static double xPos, yPos, lastY, lastX;
    private static final boolean[] mouseButtonDown = new boolean[3];
    private static boolean isDragging;

    private static Vector2f gameViewportPos = new Vector2f();
    private static Vector2f gameViewportSize= new Vector2f();

    private MouseListener(){
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
    }

    public static void mousePosCallback(long a_window, double a_xPos, double a_yPos){
        lastX = xPos;
        lastY = yPos;
        xPos = a_xPos;
        yPos = a_yPos;
        isDragging = isButtonDown(0) || isButtonDown(1) || isButtonDown(2);
    }

    public static void mouseButtonCallback(long a_window, int a_button, int a_action, int a_mods){
        if(a_action == GLFW_PRESS){
            if(a_button < mouseButtonDown.length) {
                mouseButtonDown[a_button]=true;
            }
        }else if(a_action == GLFW_RELEASE){
            if(a_button < mouseButtonDown.length){
                mouseButtonDown[a_button]=false;
                isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long a_window, double a_xOff, double a_yOff){
        scrollX = a_xOff;
        scrollY = a_yOff;
    }

    public static void reset(){
        scrollX = 0;
        scrollY = 0;
        lastX = xPos;
        lastY = yPos;
    }

    public static float getX(){
        return (float)xPos;
    }

    public static float getY(){
        return (float)yPos;
    }

    public static float getDx(){
        return (float)(lastX - xPos);
    }

    public static float getDy(){
        return (float)(lastY - yPos);
    }

    public static float getScrollX(){
        return (float)scrollX;
    }

    public static float getScrollY(){
        return (float)scrollY;
    }

    public static boolean isDragging(){
        return isDragging;
    }

    private static float getOrthoY(){
        float _currentY = getY() - gameViewportPos.y;

        // Get the -1.0 - 1.0 mouse x pos
        _currentY = -((_currentY / (float)gameViewportSize.y) * 2.0f -1.0f);

        return _currentY;
    }

    private static float getOrthoX(){
        float _currentX = getX() - gameViewportPos.x;
        // Get the -1.0 - 1.0 mouse x pos
        _currentX = (_currentX / (float)gameViewportSize.x) * 2.0f -1.0f;

        return _currentX;
    }

    public static Vector2f getOrtho(){
        Vector4f _tmp = new Vector4f(getOrthoX(), getOrthoY(), 0, 1);

        Camera _cam = Window.getCurrentScene().getCamera();
        Matrix4f _viewProj = new Matrix4f();
        _cam.getInvViewMatrix().mul(_cam.getInvProjectionMatrix(), _viewProj);
        _tmp.mul(_viewProj);

        return new Vector2f(_tmp.x, _tmp.y);
    }

    public static boolean isButtonDown(int a_button){
        if(a_button < mouseButtonDown.length){
            return mouseButtonDown[a_button];
        }

        return false;
    }

    public static void setGameViewportPos(Vector2f a_gameViewportPos) {
        gameViewportPos.set(a_gameViewportPos);
    }

    public static void setGameViewportSize(Vector2f a_gameViewportSize) {
        gameViewportSize.set(a_gameViewportSize);
    }
}
