package me.rotthin.projectm.engine.debug;

public abstract class DebugShape {
    protected float lifeTime;
    protected DebugDraw.LifeTimeType lifeTimeType;

    public DebugDraw.LifeTimeType getLifeTimeType() {
        return lifeTimeType;
    }

    public void update(float a_dt){
        lifeTime -= lifeTimeType == DebugDraw.LifeTimeType.seconds ? a_dt : 1;
    }

    public float getLifeTime(){
        return lifeTime;
    }
}
