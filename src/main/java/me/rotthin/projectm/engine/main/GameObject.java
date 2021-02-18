package me.rotthin.projectm.engine.main;

import imgui.ImGui;
import me.rotthin.projectm.engine.annotations.ShowInInspector;
import me.rotthin.projectm.engine.components.Component;
import me.rotthin.projectm.engine.editor.gui.imgui.ImGuiUtils;
import me.rotthin.projectm.engine.editor.scenes.EditorScene;
import me.rotthin.projectm.engine.input.MouseListener;
import me.rotthin.projectm.engine.rendering.Window;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class GameObject {
    private static int s_idCounter = 0;
    private int uid=-1;

    private boolean showInInspector;
    @ShowInInspector private String name;
    public Transform transform;
    private int zIndex;
    private List<Component> components;
    private transient List<Component> toRemove;

    public GameObject(String a_name){
        init(a_name, new Transform(), 0);
    }

    public GameObject(String a_name, Transform a_transform, int a_zIndex){
        init(a_name, a_transform, a_zIndex);
    }

    private void init(String a_name, Transform a_transform, int a_zIndex){
        name = a_name;
        components = new ArrayList<>();
        toRemove = new ArrayList<>();
        transform = a_transform;
        zIndex = a_zIndex;
        uid = s_idCounter++;
        setShowInInspector(true);
    }

    public <T extends Component> T getComponent(Class<T> a_type){
        for(Component _c : components){
            if(a_type.isAssignableFrom(_c.getClass())){
                try{
                    return a_type.cast(_c);
                }catch(ClassCastException e){
                    e.printStackTrace();
                    assert false: "Error: Casting component.";
                }
            }
        }

        return null;
    }

    public void removeComponent(Component a_component){
        if(components.contains(a_component)){
            toRemove.add(a_component);
        }
    }

    public Component addComponent(Component a_c){
        if(hasComponent(a_c.getClass())){ return null; }

        a_c.gameObject = this;
        a_c.genID();
        components.add(a_c);

        return a_c;
    }

    public boolean hasComponent(Class a_type){
        return components.stream().anyMatch(a_type::isInstance);
    }

    public void update(float a_dt){
        while(toRemove.size() > 0){
            if(components.contains(toRemove.get(0))){
                components.remove(toRemove.get(0));
            }

            toRemove.remove(0);
        }

        for (Component component : components) {
            component.update(a_dt);
        }
    }

    public void start(){
        for (Component component : components) {
            component.start();
        }
    }

    public void inspectorImgui(){
        ImGuiUtils.showFields(this, false);
        transform.imgui();
        for(Component c : components) c.imgui(true);
    }

    public void hierarchyImgui(){
        if(!showInInspector) return;

        if(ImGui.button(getName())){
            ((EditorScene)Window.getCurrentScene()).selectObject(this);
        }
    }

    public int getZIndex(){
        return zIndex;
    }

    public String getName(){
        return name;
    }

    public static void init(int a_maxId){
        s_idCounter = a_maxId;
    }

    public int getUid(){
        return uid;
    }

    public List<Component> getAllComponents(){
        return components;
    }

    public void setShowInInspector(boolean a_val){
        showInInspector = a_val;
    }

    public boolean mouseHoversOver(){
        Vector2f _mousePos = MouseListener.getOrtho();
        float _x = transform.position.x;
        float _y = transform.position.y;

        if((_mousePos.x >= _x) && (_mousePos.x <= _x + transform.scale.x) && (_mousePos.y >= _y) && (_mousePos.y <= _y + transform.scale.y)){
            return true;
        }

        return false;
    }
}
