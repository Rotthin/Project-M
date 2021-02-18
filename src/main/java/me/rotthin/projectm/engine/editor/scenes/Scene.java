package me.rotthin.projectm.engine.editor.scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.rotthin.projectm.engine.components.Component;
import me.rotthin.projectm.engine.main.Camera;
import me.rotthin.projectm.engine.rendering.Renderer;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.serialization.ComponentDeserializer;
import me.rotthin.projectm.engine.serialization.GameObjectDeserializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    public Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning=false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected boolean loaded=false;

    public Scene(){  }

    public void start(){
        for(GameObject _go : gameObjects){
            _go.start();
            renderer.add(_go);
        }

        isRunning = true;
    }

    public void init() {}

    public void addGameObject(GameObject a_go){
        gameObjects.add(a_go);
        if(isRunning){ a_go.start(); }
    }

    public void removeGameObject(GameObject a_go){
        if(a_go == null || !gameObjects.contains(a_go)){
            return;
        }
        gameObjects.remove(a_go);
    }

    public void update(float a_dt){ }

    public Camera getCamera(){
        return camera;
    }

    public void imgui(){
        sceneImgui();
    }

    protected void sceneImgui(){ }

    public void save(){
        Gson _gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try{
            File _dir = new File("levels");
            if(!_dir.exists()){
                _dir.mkdir();
            }

            FileWriter _wr = new FileWriter("levels/level.scn");
            _wr.write(_gson.toJson(gameObjects));
            _wr.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void load(){
        Gson _gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String _inFile = "";

        try{
            File _dir = new File("levels");
            if(!_dir.exists()){ return; }
            _inFile = new String(Files.readAllBytes(Paths.get("levels/level.scn")));
        }catch (IOException e){
            e.printStackTrace();
        }

        if(!_inFile.equals("")){
            int _maxGoId = -1;
            int _maxCompId = -1;

            GameObject[] _objs = _gson.fromJson(_inFile, GameObject[].class);
            for (GameObject _go : _objs) {
                addGameObject(_go);

                for (Component _c : _go.getAllComponents()) {
                    if (_c.getUid() > _maxCompId) {
                        _maxCompId = _c.getUid();
                    }
                }

                if (_go.getUid() > _maxGoId) {
                    _maxGoId = _go.getUid();
                }
            }

            _maxCompId++;
            _maxGoId++;
            GameObject.init(_maxGoId);
            Component.init(_maxCompId);
            loaded = true;
        }
    }
}
