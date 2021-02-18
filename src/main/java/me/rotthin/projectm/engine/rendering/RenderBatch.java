package me.rotthin.projectm.engine.rendering;

import me.rotthin.projectm.engine.components.SpriteRenderer;
import me.rotthin.projectm.engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch> {
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;
    private int zIndex;

    public RenderBatch(int a_maxBatchSize, int a_zIndex) {
        zIndex = a_zIndex;
        shader = ResourceManager.getShader("assets/shaders/default.glsl");
        sprites = new SpriteRenderer[a_maxBatchSize];
        maxBatchSize = a_maxBatchSize;

        // 4 vertices quads
        vertices = new float[a_maxBatchSize * 4 * VERTEX_SIZE];

        numSprites = 0;
        hasRoom = true;
        textures = new ArrayList<>();
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRenderer a_spr) {
        // Get index and add renderObject
        int _index = this.numSprites;
        sprites[_index] = a_spr;
        numSprites++;

        if (a_spr.getTexture() != null) {
            if (!textures.contains(a_spr.getTexture())) {
                textures.add(a_spr.getTexture());
            }
        }

        // Add properties to local vertices array
        loadVertexProperties(_index);

        if (numSprites >= this.maxBatchSize) {
            hasRoom = false;
        }
    }

    public void render() {
        boolean rebufferData = false;
        for (int i=0; i < numSprites; i++) {
            SpriteRenderer spr = sprites[i];
            if (spr.getIsDirty()) {
                loadVertexProperties(i);
                spr.setIsDirty(false);
                rebufferData = true;
            }
        }
        if (rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // Use shader
        shader.bind();
        shader.setMat4f("uProj", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.setMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());
        for (int i=0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.setIntArr("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i=0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }
        shader.unbind();
    }

    private void loadVertexProperties(int a_index) {
        SpriteRenderer _sprite = sprites[a_index];

        // Find offset within array (4 vertices per sprite)
        int _offset = a_index * 4 * VERTEX_SIZE;

        Vector4f color = _sprite.getColor();
        Vector2f[] _uvs = _sprite.getUvs();

        int _texID = 0;
        if (_sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i).equals(_sprite.getTexture())) {
                    _texID = i + 1;
                    break;
                }
            }
        }

        // Add vertices with the appropriate properties
        float _xAdd = 1.0f;
        float _yAdd = 1.0f;
        for (int i=0; i < 4; i++) {
            switch(i){
                case 1: _yAdd=0.0f;break;
                case 2: _xAdd=0.0f;break;
                case 3: _yAdd=1.0f;break;
            }

            // Load position
            vertices[_offset] = _sprite.gameObject.transform.position.x + (_xAdd * _sprite.gameObject.transform.scale.x);
            vertices[_offset + 1] = _sprite.gameObject.transform.position.y + (_yAdd * _sprite.gameObject.transform.scale.y);

            // Load color
            vertices[_offset + 2] = color.x;
            vertices[_offset + 3] = color.y;
            vertices[_offset + 4] = color.z;
            vertices[_offset + 5] = color.w;

            // Load texture coordinates
            vertices[_offset + 6] = _uvs == null ? 0 : _uvs[i].x;
            vertices[_offset + 7] = _uvs == null ? 0 : _uvs[i].y;

            // Load texture id
            vertices[_offset + 8] = _texID;

            _offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i=0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] a_elements, int a_index) {
        int offsetArrayIndex = 6 * a_index;
        int offset = 4 * a_index;

        // Triangle 1
        a_elements[offsetArrayIndex] = offset + 3;
        a_elements[offsetArrayIndex + 1] = offset + 2;
        a_elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        a_elements[offsetArrayIndex + 3] = offset + 0;
        a_elements[offsetArrayIndex + 4] = offset + 2;
        a_elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean getHasRoom() {
        return hasRoom;
    }

    public boolean hasTextureRoom() {
        return textures.size() < 8;
    }

    public boolean hasTexture(Texture a_tex) {
        return textures.contains(a_tex);
    }

    public int getZIndex() {
        return zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(zIndex, o.getZIndex());
    }
}