package net.pixeltree.project_m.engine;

import net.pixeltree.project_m.engine.input.KeyListener;
import net.pixeltree.project_m.engine.input.MouseListener;
import net.pixeltree.project_m.engine.math.Color;
import net.pixeltree.project_m.engine.scenes.LevelEditorScene;
import net.pixeltree.project_m.engine.scenes.LevelScene;
import net.pixeltree.project_m.engine.scenes.Scene;
import net.pixeltree.project_m.engine.utils.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window instance;
    private int width;
    private int height;
    private String title;
    public Color clearColor;
    private long glfwWindow;
    private static Scene currentScene;

    private Window(){
        width = 1920;
        height = 1080;
        title = "Project M";
        clearColor = Color.red();
    }

    public static Window getInstance(){
        if(instance == null) instance = new Window();
        return instance;
    }

    public static void changeScene(int a_index){
        switch(a_index){
            case 0:
                currentScene = new LevelEditorScene();
                break;

            case 1:
                currentScene = new LevelScene();
                break;

            default:
                assert false: "Tried to change to not-existing scene: '" + a_index + "'.";
                break;
        }
    }

    public void run(){
        init();
        loop();
    }

    private void init(){
        GLFWErrorCallback.createPrint(System.err).set();
        initGlfw();
        changeScene(0);
    }

    private void initGlfw(){
        if(!glfwInit()) throw new IllegalStateException("unable to init glfw.");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_MAXIMIZED);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if(glfwWindow == 0) throw new IllegalStateException("unable to create the glfw window.");

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    private void loop(){
        float _beginTime = Time.getTime();
        float _endTime;
        float _dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(_dt >= 0 || currentScene != null) currentScene.update(_dt);

            glfwSwapBuffers(glfwWindow);

            MouseListener.reset();

            _endTime = Time.getTime();
            _dt = _endTime - _beginTime;
            _beginTime = _endTime;
        }

        free();
    }

    private void free(){
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
