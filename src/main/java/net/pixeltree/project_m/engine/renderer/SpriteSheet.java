package net.pixeltree.project_m.engine.renderer;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture a_tex, int a_spriteWidth, int a_spriteHeight, int a_numSprites, int a_spacing){
        sprites = new ArrayList<>();
        texture = a_tex;

        int _currentX = 0;
        int _currentY = texture.getHeight() - a_spriteHeight;

        for(int i=0; i<a_numSprites; i++){
            float _top = (_currentY + a_spriteHeight) / (float)texture.getHeight();
            float _right = (_currentX + a_spriteWidth) / (float)texture.getWidth();
            float _left = _currentX / (float)texture.getWidth();
            float _bottom = _currentY / (float)texture.getHeight();

            Vector2f[] _uvs = {
                    new Vector2f(_right, _top),
                    new Vector2f(_right, _bottom),
                    new Vector2f(_left, _bottom),
                    new Vector2f(_left, _top)
            };

            sprites.add(new Sprite(texture, _uvs));

            _currentX += a_spriteWidth + a_spacing;
            if(_currentX >= texture.getWidth()){
                _currentX = 0;
                _currentY -= a_spriteHeight + a_spacing;
            }
        }
    }

    public Sprite getSprite(int a_index){
        return sprites.get(a_index);
    }
}
