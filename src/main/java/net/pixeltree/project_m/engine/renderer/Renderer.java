package net.pixeltree.project_m.engine.renderer;

import net.pixeltree.project_m.engine.GameObject;
import net.pixeltree.project_m.engine.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer(){
        batches = new ArrayList<>();
    }

    public void add(GameObject a_go){
        SpriteRenderer _spr = a_go.getComponent(SpriteRenderer.class);

        if(_spr != null) add(_spr);
    }

    private void add(SpriteRenderer a_sprite){
        // Check if we can add a sprite to a existing batch
        boolean _added = false;
        for(RenderBatch _batch : batches){
            if(_batch.getHasRoom()){
                Texture _tex = a_sprite.getTexture();
                if(_tex == null || _batch.hasTexture(_tex) || _batch.hasTextureRoom()){
                    _batch.addSprite(a_sprite);
                    _added = true;
                    break;
                }
            }
        }
        // Else, create new one and add the sprite to it
        if(!_added){
            RenderBatch _newBatch = new RenderBatch(MAX_BATCH_SIZE);
            _newBatch.start();
            batches.add(_newBatch);
            _newBatch.addSprite(a_sprite);
        }
    }

    public void render(){
        for(RenderBatch _batch : batches) _batch.render();
    }
}
