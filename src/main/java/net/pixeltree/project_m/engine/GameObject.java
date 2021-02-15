package net.pixeltree.project_m.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    public Transform transform;

    private List<Component> components;

    public GameObject(String a_name){
        init(a_name, new Transform());
    }

    public GameObject(String a_name, Transform a_transform){
        init(a_name, a_transform);
    }

    private void init(String a_name, Transform a_transform){
        name = a_name;
        components = new ArrayList<>();
        transform = a_transform;
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

    public <T extends Component> void removeComponent(Class<T> a_type){
        for(int i=0; i<components.size(); i++){
            Component _c = components.get(i);
            if(a_type.isAssignableFrom(_c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component a_c){
        components.add(a_c);
        a_c.gameObject = this;
    }

    public void update(float a_dt){
        for(int i=0; i<components.size(); i++){
            components.get(i).update(a_dt);
        }
    }

    public void start(){
        for(int i=0; i<components.size(); i++){
            components.get(i).start();
        }
    }
}
