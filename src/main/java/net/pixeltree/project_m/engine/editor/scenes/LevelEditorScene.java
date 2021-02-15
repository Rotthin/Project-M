package net.pixeltree.project_m.engine.editor.scenes;

import net.pixeltree.project_m.engine.GameObject;
import net.pixeltree.project_m.engine.Scene;
import net.pixeltree.project_m.engine.Transform;
import net.pixeltree.project_m.engine.components.SpriteRenderer;
import net.pixeltree.project_m.engine.input.Input;
import net.pixeltree.project_m.engine.input.KeyListener;
import net.pixeltree.project_m.engine.math.Camera;
import net.pixeltree.project_m.engine.renderer.Sprite;
import net.pixeltree.project_m.engine.utils.ResourceManager;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {
    private GameObject go1, go2, go3, go4;

    public LevelEditorScene(){  }

    @Override
    public void init(){
        loadResources();

        camera = new Camera(new Vector2f());

        go1 = new GameObject("go1", new Transform(new Vector2f(100,100), new Vector2f(256, 256)));
        go1.addComponent(new SpriteRenderer(new Sprite(ResourceManager.getTexture("assets/textures/blendImage1.png"))));
        addGameObject(go1);

        go2 = new GameObject("go2", new Transform(new Vector2f(400,100), new Vector2f(256, 256)));
        go2.addComponent(new SpriteRenderer(new Sprite(ResourceManager.getTexture("assets/textures/blendImage2.png"))));
        addGameObject(go2);
    }

    @Override
    public void update(float a_dt) {
        if(Input.isKeyDown(GLFW_KEY_D)) go2.transform.position.x += a_dt*200;
        if(Input.isKeyDown(GLFW_KEY_A)) go2.transform.position.x -= a_dt*200;
        if(Input.isKeyDown(GLFW_KEY_W)) go2.transform.position.y += a_dt*200;
        if(Input.isKeyDown(GLFW_KEY_S)) go2.transform.position.y -= a_dt*200;

        for(GameObject go : gameObjects) go.update(a_dt);
        renderer.render();
    }

    private void loadResources(){
        ResourceManager.getShader("assets/shaders/default.glsl");
        ResourceManager.getTexture("assets/textures/blendImage1.png");
        ResourceManager.getTexture("assets/textures/blendImage2.png");
    }
}