package me.rotthin.projectm.engine.editor.gui;

import me.rotthin.projectm.engine.debug.DebugDraw;
import me.rotthin.projectm.engine.input.MouseListener;

public final class Cursor {
    private Cursor() {}

    private static float rotation;

    public static void update(float a_dt){
        rotation += a_dt * 100;
        while(rotation >= 360){
            rotation -= 360;
        }

        DebugDraw.addCircle(MouseListener.getOrtho(), 20, 1, DebugDraw.LifeTimeType.frames);
    }
}
