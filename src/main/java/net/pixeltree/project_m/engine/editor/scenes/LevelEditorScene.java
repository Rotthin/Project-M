package net.pixeltree.project_m.engine.editor.scenes;

import net.pixeltree.project_m.engine.GameObject;
import net.pixeltree.project_m.engine.Transform;
import net.pixeltree.project_m.engine.components.SpriteRenderer;
import net.pixeltree.project_m.engine.math.Camera;
import net.pixeltree.project_m.engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {
    public LevelEditorScene(){  }

    @Override
    public void init(){
        loadResources();

        camera = new Camera(new Vector2f());

        GameObject go = new GameObject("Test Sprite", new Transform(new Vector2f(400,100), new Vector2f(256, 256)), -1);
        go.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
        addGameObject(go);

        activeGameObject = go;
    }

    @Override
    public void update(float a_dt) {
        for(GameObject go : gameObjects) go.update(a_dt);
        renderer.render();
    }

    @Override
    protected void sceneImgui(){
    }

    private void loadResources(){
        ResourceManager.getShader("assets/shaders/default.glsl");
    }
}