package me.rotthin.projectm.engine.rendering;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filePath;
    private transient int id;
    private int width, height;

    public Texture(){
        id = -1;
        width = -1;
        height = -1;
    }

    public Texture(int a_width, int a_height){
        // Store the file path for debugging
        filePath = "Generated";

        // Generate texture
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, a_width, a_height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

    }

    public void init(String a_filePath){
        // Store the file path for debugging
        filePath = a_filePath;

        // Generate texture
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Set texture parameters
        // Repeat texture when uv is bigger than 1 or smaller than 0
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When stretching the image, pixelate it, not blur
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking the image, pixelate it, not blur
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer _width = BufferUtils.createIntBuffer(1);
        IntBuffer _height = BufferUtils.createIntBuffer(1);
        IntBuffer _channels = BufferUtils.createIntBuffer(1);

        // Flip the image vertically on load
        stbi_set_flip_vertically_on_load(true);
        // Load the image from file
        ByteBuffer _img = stbi_load(filePath, _width, _height, _channels, 0);

        if(_img != null){
            width = _width.get(0);
            height = _height.get(0);

            if(_channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, _width.get(0), _height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, _img);
            }else if(_channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, _width.get(0), _height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, _img);
            }else assert false: "ERROR (Texture): Unknown number of channels: " + _channels.get(0) + "file: " + filePath + "'";
        }else assert false: "ERROR (Texture): Couldn't load image '" + filePath + "'";

        // Free the memory
        stbi_image_free(_img);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String  getFilePath(){
        return filePath;
    }

    public int getID() {
        return id;
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Texture)) return false;

        Texture _tex = (Texture)o;
        return _tex.getWidth() == width && _tex.getHeight() == height && _tex.getID() == id && _tex.getFilePath().equals(filePath);
    }
}
