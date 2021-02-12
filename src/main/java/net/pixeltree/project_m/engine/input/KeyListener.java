package net.pixeltree.project_m.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keysDown[] = new boolean[350];

    private KeyListener() {  }

    public static KeyListener getInstance(){
        checkInstanceNull();
        return instance;
    }

    public static void keyCallback(long a_window, int a_key, int a_scanCode, int a_action, int a_mods){
        checkInstanceNull();
        if(a_action == GLFW_PRESS) getInstance().keysDown[a_key]=true;
        else if(a_action == GLFW_RELEASE) getInstance().keysDown[a_key]=false;
    }

    public static boolean isKeyDown(int a_key){
        checkInstanceNull();
        if(a_key < instance.keysDown.length) return instance.keysDown[a_key];
        return false;
    }

    private static void checkInstanceNull(){
        if(instance == null) instance = new KeyListener();
    }
}
