package me.rotthin.projectm.engine.rendering;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {
    private int fbo=0;
    private Texture texture=null;

    public FrameBuffer(int a_width, int a_height){
        // Gen framebuffer
        fbo = glGenFramebuffers();
        bind();

        // Create the texture to render the data to and attach it to the framebuffer
        texture = new Texture(a_width, a_height);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getID(), 0);

        // Create renderbuffer to store depth info
        int _rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, _rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, a_width, a_height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, _rbo);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            assert false: "Error: frame buffer is not complete";
        }

        unbind();
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
    }

    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFbo(){
        return fbo;
    }

    public int getTextureID(){
        return texture.getID();
    }
}
