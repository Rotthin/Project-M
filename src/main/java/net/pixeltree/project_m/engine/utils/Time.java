package net.pixeltree.project_m.engine.utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    public static float getTime(){
        return (float)glfwGetTime();
    }
}
