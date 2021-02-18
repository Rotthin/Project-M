package me.rotthin.projectm.engine.other;

import me.rotthin.projectm.engine.components.SpriteRenderer;
import me.rotthin.projectm.engine.main.GameObject;
import me.rotthin.projectm.engine.main.Transform;
import me.rotthin.projectm.engine.rendering.Sprite;
import me.rotthin.projectm.engine.utils.Constants;
import org.joml.Vector2f;

import java.util.UUID;

public final class Prefabs {
    private Prefabs() {}

    public static GameObject createBlock(Sprite a_spr, float a_sizeX, float a_sizeY){
        GameObject _block = new GameObject("Block " + UUID.randomUUID(),
                new Transform(new Vector2f(), new Vector2f(a_sizeX, a_sizeY)),0);

        SpriteRenderer _sr = new SpriteRenderer(a_spr);
        _block.addComponent(_sr);

        return _block;
    }
}
