package me.rotthin.projectm.engine.components;

import me.rotthin.projectm.engine.input.MouseListener;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.renderer.Window;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    private GameObject holding = null;

    public void pickupBlock(GameObject a_go){
        holding = a_go;
        Window.getCurrentScene().addGameObject(a_go);
    }

    public void placeBlock(){
        holding = null;
    }

    @Override
    public void update(float a_dt){
        if (holding != null) {
            Vector2f _mousePos = MouseListener.getOrtho();
            holding.transform.position.x = _mousePos.x-32;
            holding.transform.position.y = _mousePos.y-32;

            if(MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                placeBlock();
            }
        }
    }
}
