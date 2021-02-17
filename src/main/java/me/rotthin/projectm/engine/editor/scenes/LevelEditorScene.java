package me.rotthin.projectm.engine.editor.scenes;

import imgui.ImGui;
import me.rotthin.projectm.engine.main.Transform;
import me.rotthin.projectm.engine.math.Camera;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.components.SpriteRenderer;
import me.rotthin.projectm.engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {
    @Override
    public void init(){
        loadResources();

        camera = new Camera(new Vector2f());

        if(loaded) return;

        GameObject go = new GameObject("Game Object 1", new Transform(new Vector2f(400,100), new Vector2f(256, 256)), -1);
        go.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
        addGameObject(go);

        GameObject go2 = new GameObject("Game Object 2", new Transform(new Vector2f(100,100), new Vector2f(256, 256)), -1);
        go2.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
        addGameObject(go2);

        selectedObject = go;
    }

    @Override
    public void update(float a_dt) {
        for(GameObject go : gameObjects) go.update(a_dt);
        renderer.render();
    }

    @Override
    protected void sceneImgui(){
        ImGui.begin("Inspector");
        if(selectedObject != null) selectedObject.inspectorImgui();
        ImGui.end();

        ImGui.begin("Hierarchy");
        for(GameObject _obj : gameObjects) _obj.hierarchyImgui();
        ImGui.end();
    }

    private void loadResources(){
        ResourceManager.getShader("assets/shaders/default.glsl");
    }

    public void selectObject(GameObject a_obj){
        selectedObject = a_obj;
    }
}