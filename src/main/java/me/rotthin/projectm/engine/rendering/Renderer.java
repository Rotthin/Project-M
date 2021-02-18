package me.rotthin.projectm.engine.rendering;

import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private final List<RenderBatch> batches;

    public Renderer(){
        batches = new ArrayList<>();
    }

    public void add(GameObject a_go){
        add(a_go.getComponent(SpriteRenderer.class));
    }

    public void add(SpriteRenderer a_sprite){
        if(a_sprite == null) return;

        // Check if we can add a sprite to a existing batch
        boolean _added = false;
        for(RenderBatch _batch : batches){
            if(_batch.getHasRoom() && _batch.getZIndex() == a_sprite.gameObject.getZIndex()){
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
            RenderBatch _newBatch = new RenderBatch(MAX_BATCH_SIZE, a_sprite.gameObject.getZIndex());
            _newBatch.start();
            _newBatch.addSprite(a_sprite);

            batches.add(_newBatch);
            Collections.sort(batches);
        }
    }

    public void render(){
        for(RenderBatch _batch : batches) _batch.render();
    }
}
