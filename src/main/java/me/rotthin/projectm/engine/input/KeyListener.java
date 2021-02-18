package me.rotthin.projectm.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public final class KeyListener {
    private static final boolean[] keysDown = new boolean[350];

    private KeyListener() {  }

    public static void keyCallback(long a_window, int a_key, int a_scanCode, int a_action, int a_mods){
        if(a_action == GLFW_PRESS) keysDown[a_key]=true;
        else if(a_action == GLFW_RELEASE) keysDown[a_key]=false;
    }

    public static boolean isKeyDown(int a_key){
        if(a_key < keysDown.length) return keysDown[a_key];
        return false;
    }
}
