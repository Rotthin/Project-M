package me.rotthin.projectm.engine.components;

import me.rotthin.projectm.engine.debug.DebugDraw;
import me.rotthin.projectm.engine.debug.DebugLog;
import me.rotthin.projectm.engine.input.MouseListener;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.math.JMath;
import me.rotthin.projectm.engine.rendering.Window;
import me.rotthin.projectm.engine.utils.Constants;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public final class MouseControls {
    private MouseControls() {}

    private static GameObject holding;
    private static Vector2f mousePosGrid = new Vector2f();

    public static void pickupBlock(GameObject a_go){
        holding = a_go;
        Window.getCurrentScene().addGameObject(a_go);
        a_go.setShowInInspector(true);
    }

    public static void placeBlock(){
        DebugLog.log("Block placed at pos: " + JMath.vector2fToString(holding.transform.position));
        holding = null;
    }

    public static void update(float a_dt){
        Vector2f _mouseOrtho = MouseListener.getOrtho();

        mousePosGrid.x = (int)(_mouseOrtho.x / Constants.GRID_WIDTH) * Constants.GRID_WIDTH;
        mousePosGrid.y = (int)(_mouseOrtho.y / Constants.GRID_HEIGHT) * Constants.GRID_HEIGHT;

        if (holding != null) {
            holding.transform.position.x = mousePosGrid.x;
            holding.transform.position.y = mousePosGrid.y;

            if(MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                placeBlock();
            }
        }
    }
}
