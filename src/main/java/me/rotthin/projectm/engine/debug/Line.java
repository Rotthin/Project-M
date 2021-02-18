package me.rotthin.projectm.engine.debug;

import org.joml.*;

public class Line extends DebugShape{
    private Vector2f start;
    private Vector2f end;
    private Vector3f color;

    public Line(Vector2f a_start, Vector2f a_end, Vector3f a_color, float a_lifeTime, DebugDraw.LifeTimeType a_type){
        start = a_start;
        end = a_end;
        color = a_color;
        lifeTime = a_lifeTime;
        lifeTimeType = a_type;
    }

    public Vector2f getStart(){
        return start;
    }

    public Vector2f getEnd(){
        return end;
    }

    public Vector3f getColor(){
        return color;
    }
}
