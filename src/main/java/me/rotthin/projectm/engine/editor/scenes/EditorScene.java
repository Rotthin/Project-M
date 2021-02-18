package me.rotthin.projectm.engine.editor.scenes;

import imgui.ImGui;
import imgui.ImVec2;
import me.rotthin.projectm.engine.components.*;
import me.rotthin.projectm.engine.debug.DebugDraw;
import me.rotthin.projectm.engine.editor.gui.Cursor;
import me.rotthin.projectm.engine.input.Input;
import me.rotthin.projectm.engine.main.Camera;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.other.Prefabs;
import me.rotthin.projectm.engine.rendering.Sprite;
import me.rotthin.projectm.engine.rendering.SpriteSheet;
import me.rotthin.projectm.engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class EditorScene extends Scene {
    private GameObject selectedObject = null;
    private final Map<String, Class> components = new HashMap<>();
    private SpriteSheet blocks;

    private GameObject editorCompHolder = new GameObject("LevelEditor");

    @Override
    public void init(){
        loadResources();

        blocks = ResourceManager.getSpriteSheet("assets/textures/blocks.png");
        components.put("Sprite Renderer", SpriteRenderer.class);
        editorCompHolder.addComponent(new Grid());
        camera = new Camera(new Vector2f());
    }

    @Override
    public void update(float a_dt) {
        editorCompHolder.update(a_dt);

        if(Input.isMouseBtnDown(GLFW_MOUSE_BUTTON_LEFT)){
            for(GameObject _go : gameObjects){
                if(_go.mouseHoversOver()){
                    selectedObject = _go;
                    break;
                }
            }
        }

        if(selectedObject != null){
            Vector2f _center = new Vector2f();
            _center.x = selectedObject.transform.position.x + (selectedObject.transform.scale.x/2f);
            _center.y = selectedObject.transform.position.y + (selectedObject.transform.scale.y/2f);
            DebugDraw.addBox(_center, selectedObject.transform.scale);
        }

        for(GameObject go : gameObjects) go.update(a_dt);
        renderer.render();

        Cursor.update(a_dt);
    }

    @Override
    protected void sceneImgui(){
        imguiInspector();
        imguiHierarchy();
        imguiBlocks();
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

                            if(selectedObject.hasComponent(_type)){ continue; }

                            Object _obj = _type.newInstance();
                            Component _c = (Component)_obj;

                            selectedObject.addComponent(_c).start();
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
        ImGui.begin( "Hierarchy");
        for(GameObject _obj : gameObjects) _obj.hierarchyImgui();
            if(ImGui.button("Create new GameObject")){
                GameObject _new = new GameObject(UUID.randomUUID().toString());
                addGameObject(_new);
                selectedObject = _new;
            }
        ImGui.end();
    }

    private void imguiBlocks(){
        ImGui.begin("Blocks");

        ImVec2 _winPos = new ImVec2();
        ImGui.getWindowPos(_winPos);

        ImVec2 _winSize = new ImVec2();
        ImGui.getWindowSize(_winSize);

        ImVec2 _itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(_itemSpacing);

        float _windowX2= _winPos.x + _winSize.x;
        for (int i=0; i < blocks.getSize(); i++) {
            Sprite _s = blocks.getSprite(i);
            float _sWidth = _s.getWidth() * 2;
            float _sHeight = _s.getHeight() * 2;
            int _id = _s.getTextureID();
            Vector2f[] _uv = _s.getUvs();

            ImGui.pushID(i);
            if (ImGui.imageButton(_id, _sWidth, _sHeight, _uv[2].x, _uv[0].y, _uv[0].x, _uv[2].y)) {
                GameObject _go = Prefabs.createBlock(_s, 32, 32);
                MouseControls.pickupBlock(_go);
            }
            ImGui.popID();

            ImVec2 _lastBtnPos = new ImVec2();
            ImGui.getItemRectMax(_lastBtnPos);
            float _lastBtnPosX2 = _lastBtnPos.x;
            float _nextButtonPosX2 = _lastBtnPosX2 + _itemSpacing.x + _sWidth;
            if (i + 1 < blocks.getSize() && _nextButtonPosX2 < _windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }

    private void loadResources(){
        ResourceManager.getShader("assets/shaders/default.glsl");
        ResourceManager.getShader("assets/shaders/debugLine.glsl");
        ResourceManager.addSpriteSheet("assets/textures/blocks.png", new SpriteSheet(ResourceManager.getTexture("assets/textures/blocks.png"), 16, 16, 81, 0));

        for(GameObject _g : gameObjects){
            SpriteRenderer _sr = _g.getComponent(SpriteRenderer.class);
            if(_sr != null && _sr.getTexture() != null){
                _sr.setTexture(ResourceManager.getTexture(_sr.getTexture().getFilePath()));
            }
        }
    }

    public void selectObject(GameObject a_obj){
        selectedObject = a_obj;
    }

    public GameObject getSelectedObject(){
        return selectedObject;
    }
}