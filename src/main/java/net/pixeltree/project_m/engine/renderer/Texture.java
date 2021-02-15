package net.pixeltree.project_m.engine.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filePath;
    private int texture;
    private int width, height;

    public Texture(String a_filePath){
        filePath = a_filePath;

        // Generate texture
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

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
        glBindTexture(GL_TEXTURE_2D, texture);
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
}
