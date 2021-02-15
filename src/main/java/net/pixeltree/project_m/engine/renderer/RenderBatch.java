package net.pixeltree.project_m.engine.renderer;

import net.pixeltree.project_m.engine.Window;
import net.pixeltree.project_m.engine.components.SpriteRenderer;
import net.pixeltree.project_m.engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int UVS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int UVS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = UVS_OFFSET + UVS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures;
    private int vao, vbo;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int a_maxBatchSize){
        maxBatchSize = a_maxBatchSize;

        // Get the shader from resource manager
        shader = ResourceManager.getShader("assets/shaders/default.glsl");

        sprites = new SpriteRenderer[a_maxBatchSize];

        // Vertices quads
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        numSprites = 0;
        hasRoom = true;
        textures = new ArrayList<>();
    }

    public void start(){
        // Generate and bind vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Allocate space for the vertices
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buff
        int _ebo = glGenBuffers();
        int[] _indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, _indices, GL_STATIC_DRAW);

        // Enable the buffer attrib pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, UVS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, UVS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(SpriteRenderer a_spr){
        // Get the index and add the sprite renderer
        int _index = numSprites;
        sprites[_index] = a_spr;
        numSprites++;

        // Add the texture to the list if it's not null and the list doesn't contain it.
        if(a_spr.getTexture() != null && !textures.contains(a_spr.getTexture())){
            textures.add(a_spr.getTexture());
        }

        // Add properties to local vertices array
        loadVertexProperties(_index);

        hasRoom = numSprites < maxBatchSize;
    }

    public void render(){
        boolean _rebufferData = false;

        // If any sprite is dirty, rebuffer the data
        for(int i=0; i<numSprites; i++){
            SpriteRenderer _spr = sprites[i];

            if(_spr.getIsDirty()){
                loadVertexProperties(i);
                _spr.setIsDirty(false);
                _rebufferData = true;
            }
        }

        // Rebuffer all data if needed
        if(_rebufferData){
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // Bind the shader
        shader.bind();
        shader.setMat4f("uProj", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.setMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());
        shader.setIntArr("uTextures", texSlots);

        /// Bind the textures
        for(int i=0; i<textures.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        // Bind the vao and enable the attrib arrays
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the elements         6 indices per quad
        glDrawElements(GL_TRIANGLES, numSprites*6, GL_UNSIGNED_INT, 0);

        // Unbind the vao and disable the attrib arrays
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind the shader and textures
        shader.unbind();
        for(Texture _tex : textures) _tex.unbind();
    }

    private void loadVertexProperties(int a_index) {
        SpriteRenderer _spr = sprites[a_index];

        // Find offset within array (4 vertices per sprite
        int _offset = a_index * 4 * VERTEX_SIZE;

        Vector4f _color = _spr.getColor();
        Vector2f[] _uvs = _spr.getUvs();

        // Find the tex id;
        int _texID = 0;
        if(_spr.getTexture() != null){
            for(int i=0; i<textures.size(); i++){
                if(textures.get(i) == _spr.getTexture()){
                    _texID = i+1;
                    break;
                }
            }
        }

        // Add vertices with the appropriate properties
        float _xAdd = 1.0f;
        float _yAdd = 1.0f;
        for(int i=0; i<4; i++){
            switch(i){
                case 1: _yAdd = 0.0f; break;
                case 2: _xAdd = 0.0f; break;
                case 3: _yAdd = 1.0f; break;
            }

            // Load position
            vertices[_offset] = _spr.gameObject.transform.position.x + (_xAdd * _spr.gameObject.transform.scale.x);
            vertices[_offset+1] = _spr.gameObject.transform.position.y + (_yAdd * _spr.gameObject.transform.scale.y);

            // Load color
            vertices[_offset+2] = _color.x;
            vertices[_offset+3] = _color.y;
            vertices[_offset+4] = _color.z;
            vertices[_offset+5] = _color.w;

            // Load uvs
            if(_uvs != null){
                vertices[_offset+6] = _uvs[i].x;
                vertices[_offset+7] = _uvs[i].y;
            }else{
                vertices[_offset+6] = 0;
                vertices[_offset+7] = 0;
            }

            // Load texture id
            vertices[_offset+8] = _texID;

            // Move the offset by vertex size
            _offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices(){
        // 6 indices per quad (3 per triangle)
        int[] _elements = new int[6*maxBatchSize];
        for(int i=0; i<maxBatchSize; i++){
            loadElementIndices(_elements, i);
        }

        return _elements;
    }

    private void loadElementIndices(int[] a_elements, int a_index){
        int _offsetArrayIndex = 6 * a_index;
        int _offset = 4 * a_index;

        // Triangle 1
        a_elements[_offsetArrayIndex] = _offset + 3;
        a_elements[_offsetArrayIndex+1] = _offset + 2;
        a_elements[_offsetArrayIndex+2] = _offset + 0;

        // Triangle 2
        a_elements[_offsetArrayIndex+3] = _offset + 0;
        a_elements[_offsetArrayIndex+4] = _offset + 2;
        a_elements[_offsetArrayIndex+5] = _offset + 1;
    }

    public boolean getHasRoom() {
        return hasRoom;
    }

    public boolean hasTextureRoom(){
        return textures.size() < 8;
    }

    public boolean hasTexture(Texture a_tex){
        return textures.contains(a_tex);
    }
}