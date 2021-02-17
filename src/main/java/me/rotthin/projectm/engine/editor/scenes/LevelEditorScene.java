package me.rotthin.projectm.engine.editor.scenes;

import imgui.ImGui;
import imgui.ImVec2;
import me.rotthin.projectm.engine.Prefabs;
import me.rotthin.projectm.engine.components.Component;
import me.rotthin.projectm.engine.components.MouseControls;
import me.rotthin.projectm.engine.components.Rigidbody;
import me.rotthin.projectm.engine.components.SpriteRenderer;
import me.rotthin.projectm.engine.math.Camera;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.renderer.Sprite;
import me.rotthin.projectm.engine.renderer.SpriteSheet;
import me.rotthin.projectm.engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelEditorScene extends Scene {
    private GameObject selectedObject = null;
    private final Map<String, Class> components = new HashMap<>();
    private SpriteSheet blocks;

    private MouseControls mouseControls = new MouseControls();

    @Override
    public void init(){
        loadResources();

        components.put("Rigidbody", Rigidbody.class);
        components.put("Sprite Renderer", SpriteRenderer.class);

        blocks = ResourceManager.getSpriteSheet("assets/textures/blocks.png");

        camera = new Camera(new Vector2f());
    }

    @Override
    public void update(float a_dt) {
        mouseControls.update(a_dt);
        for(GameObject go : gameObjects) go.update(a_dt);
        renderer.render();
    }

    @Override
    protected void sceneImgui(){
        imguiInspector();
        imguiHierarchy();
        imguiBlocks();
    }

    private void imguiBlocks(){
        ImGui.begin("Blocks");

        ImVec2 _windowPos = new ImVec2();
        ImGui.getWindowPos(_windowPos);

        ImVec2 _windowSize = new ImVec2();
        ImGui.getWindowSize(_windowSize);

        ImVec2 _itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(_itemSpacing);

        float _windowX2 = _windowPos.x + _windowSize.x;
        for(int i=0; i<blocks.getSize(); i++){
            Sprite _spr = blocks.getSprite(i);
            float _sprWidth = _spr.getWidth() * 4;
            float _sprHeight = _spr.getHeight() * 4;
            int _id = _spr.getTextureID();
            Vector2f[] _uvs = _spr.getUvs();

            ImGui.pushID(i);
            if(ImGui.imageButton(_id, _sprWidth/2, _sprHeight/2, _uvs[2].x, _uvs[0].y, _uvs[0].x, _uvs[2].y)){
                GameObject _block = Prefabs.createBlock(_spr, _sprWidth, _sprHeight);
                mouseControls.pickupBlock(_block);
            }
            ImGui.popID();

            ImVec2 _lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(_lastButtonPos);
            float _nextButtonX2 = _lastButtonPos.x + _itemSpacing.x + _sprWidth;
            if(i+1 < blocks.getSize() && _nextButtonX2 < _windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }

    private void imguiInspector(){
        ImGui.begin("Inspector");
        if(selectedObject != null) {
            selectedObject.inspectorImgui();

            if(ImGui.beginCombo("Add Component", "Choose")){
                for(String _s : components.keySet()){
                    if(ImGui.selectable((_s))){
                        try {
                            Class _type = components.get(_s);
                            Object _obj = _type.newInstance();
                            Component _c = (Component)_obj;
                            selectedObject.addComponent(_c);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                ImGui.endCombo();
            }

            if(ImGui.button("Delete Object")){
                removeGameObject(selectedObject);
                selectedObject = null;
            }
        }
        ImGui.end();
    }

    private void imguiHierarchy(){
        ImGui.begin("Hierarchy");
        for(GameObject _obj : gameObjects) _obj.hierarchyImgui();
        if(ImGui.button("Create new GameObject")){
            GameObject _new = new GameObject(UUID.randomUUID().toString());
            addGameObject(_new);
            selectedObject = _new;
        }
        ImGui.end();
    }

    private void loadResources(){
        ResourceManager.getShader("assets/shaders/default.glsl");
        ResourceManager.addSpriteSheet("assets/textures/blocks.png", new SpriteSheet(ResourceManager.getTexture("assets/textures/blocks.png"), 16, 16, 81, 0));
    }

    public void selectObject(GameObject a_obj){
        selectedObject = a_obj;
    }
}