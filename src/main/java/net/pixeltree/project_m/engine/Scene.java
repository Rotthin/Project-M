package net.pixeltree.project_m.engine;

import net.pixeltree.project_m.engine.math.Camera;
import net.pixeltree.project_m.engine.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning=false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){ }

    public void start(){
        for(GameObject _go : gameObjects){
            _go.start();
            renderer.add(_go);
        }

        isRunning = true;
    }

    public void init(){

    }

    public void addGameObject(GameObject a_go){
        gameObjects.add(a_go);

        if(isRunning){
            a_go.start();
            renderer.add(a_go);
        }
    }

    public abstract void update(float a_dt);

    public Camera getCamera(){
        return camera;
    }
}
