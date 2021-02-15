package net.pixeltree.project_m.engine;

import net.pixeltree.project_m.engine.input.KeyListener;
import net.pixeltree.project_m.engine.input.MouseListener;
import net.pixeltree.project_m.engine.editor.scenes.LevelEditorScene;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static Window instance;
    private int width;
    private int height;
    private String title;
    public Vector4f clearColor;
    private long glfwWindow;
    private static Scene currentScene;

    private Window(){
        width = 1920;
        height = 1080;
        title = "Project M";
        clearColor = new Vector4f(1, 1, 1, 1);
    }

    public static Window getInstance(){
        if(instance == null) instance = new Window();
        return instance;
    }

    public static Scene getCurrentScene(){
        return currentScene;
    }

    public static void changeScene(int a_index){
        switch(a_index){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;

//            case 1:
//                currentScene = new LevelScene();
//                currentScene.init();
//                currentScene.start();
//                break;

            default:
                assert false: "tried to change to non-existing scene: '" + a_index + "'.";
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
        initGl();

        changeScene(0);
    }

    private void initGlfw(){
        if(!glfwInit()) throw new IllegalStateException("unable to init glfw.");

        // Set window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_MAXIMIZED);

        // Create window
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if(glfwWindow == 0) throw new IllegalStateException("unable to create the glfw window.");

        // Set callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);
    }

    private void initGl(){
        GL.createCapabilities();
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void loop(){
        float _beginTime = (float)glfwGetTime();
        float _endTime;
        float _dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            if(_dt >= 0 || currentScene != null) currentScene.update(_dt);

            glfwSwapBuffers(glfwWindow);

            // Reset the MoueListener temp values
            MouseListener.reset();

            // Calculate delta
            _endTime = (float)glfwGetTime();
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
