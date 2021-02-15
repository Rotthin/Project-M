package net.pixeltree.project_m.engine.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int program;
    private boolean isUsed;
    private String vertSrc;
    private String fragSrc;
    private String filePath;

    public Shader(String a_filePath){
        filePath = a_filePath;

        try{
            // Get raw text value form file
            String _raw = new String(Files.readAllBytes(Paths.get(filePath)));

            // Split the raw text using regex
            String[] _split = _raw.split("(#type)( )+([a-zA-Z]+)");

            // Find the first #type pattern
            int _index = _raw.indexOf("#type")+6;
            int _endOfLine = _raw.indexOf("\r\n", _index);
            String _firstPattern = _raw.substring(_index, _endOfLine).trim();

            // Find the second #type pattern
            _index = _raw.indexOf("#type", _endOfLine)+6;
            _endOfLine = _raw.indexOf("\r\n", _index);
            String _secondPattern = _raw.substring(_index, _endOfLine).trim();

            // Check what shader type the first pattern is
            if(_firstPattern.equalsIgnoreCase("vert")) vertSrc = _split[1];
            else if(_firstPattern.equals("frag")) fragSrc = _split[1];
            else throw new IOException("Unexpected token '" + _firstPattern);

            // Check what shader type the second pattern is
            if(_secondPattern.equalsIgnoreCase("vert")) vertSrc = _split[2];
            else if(_secondPattern.equals("frag")) fragSrc = _split[2];
            else throw new IOException("Unexpected token " + _secondPattern);
        }catch(IOException e){
            e.printStackTrace();
            assert false: "Error: Couldn't open shader file: '" + filePath + "'";
        }
    }

    public void compile(){
        int _vertId, _fragID;

        // Compile the vertex shader
        _vertId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(_vertId, vertSrc);
        glCompileShader(_vertId);

        // Check for vertex shader compilation error
        if(glGetShaderi(_vertId, GL_COMPILE_STATUS) == GL_FALSE){
            int _length = glGetShaderi(_vertId, GL_INFO_LOG_LENGTH);
            assert false: glGetShaderInfoLog(_vertId, _length);
        }

        // Compile the fragment shader
        _fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(_fragID, fragSrc);
        glCompileShader(_fragID);

        // Check for fragment shader compilation error
        if(glGetShaderi(_fragID, GL_COMPILE_STATUS) == GL_FALSE){
            int _length = glGetShaderi(_fragID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filePath +"'\n\tFrag shader compilation failed.");
            System.err.println(glGetShaderInfoLog(_fragID, _length));
            assert false: "";
        }

        // Create the program and link the shaders
        program = glCreateProgram();
        glAttachShader(program, _vertId);
        glAttachShader(program, _fragID);
        glLinkProgram(program);

        // Check for linking error
        if(glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE){
            int _length = glGetProgrami(program, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: '" + filePath +"'\n\tLinking of shaders failed.");
            System.err.println(glGetProgramInfoLog(program, _length));
            assert false: "";
        }
    }

    public void bind(){
        if(isUsed) return;
        isUsed = true;
        glUseProgram(program);
    }

    public void unbind(){
        glUseProgram(0);
        isUsed = false;
    }

    public void setMat4f(String a_name, Matrix4f a_mat){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure that this shader is being currently used
        bind();

        // Create and set float buffer that contains the matrix
        FloatBuffer _matBuffer = BufferUtils.createFloatBuffer(16);
        a_mat.get(_matBuffer);

        // Set the uniform matrix value
        glUniformMatrix4fv(_loc, false, _matBuffer);
    }

    public void setMat3f(String a_name, Matrix3f a_mat){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure that this shader is being currently used
        bind();

        // Create and set float buffer that contains the matrix
        FloatBuffer _matBuffer = BufferUtils.createFloatBuffer(9);
        a_mat.get(_matBuffer);

        // Set the uniform matrix value
        glUniformMatrix3fv(_loc, false, _matBuffer);
    }

    public void setVec4f(String a_name, Vector4f a_vec){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure that this shader is being currently used
        bind();

        // Set the uniform vec value
        glUniform4f(_loc, a_vec.x, a_vec.y, a_vec.z, a_vec.w);
    }

    public void setVec3f(String a_name, Vector3f a_vec){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure that this shader is being currently used
        bind();

        // Set the uniform vec value
        glUniform3f(_loc, a_vec.x, a_vec.y, a_vec.z);
    }

    public void setVec2f(String a_name, Vector2f a_vec){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure that this shader is being currently used
        bind();

        // Set the uniform vec4 value
        glUniform2f(_loc, a_vec.x, a_vec.y);
    }

    public void setFloat(String a_name, float a_val){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure this shader is being currently used
        bind();

        // Set the uniform value to the one specified
        glUniform1f(_loc, a_val);
    }

    public void setInt(String a_name, int a_val){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure this shader is being currently used
        bind();

        // Set the uniform value to the one specified
        glUniform1i(_loc, a_val);
    }

    public void setTex(String a_name, int a_slot){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure this shader is being currently used
        bind();

        // Set the uniform value to the one specified
        glUniform1i(_loc, a_slot);
    }

    public void setIntArr(String a_name, int[] a_arr){
        // Get the uniform location
        int _loc = glGetUniformLocation(program, a_name);

        // Make sure this shader is being currently used
        bind();

        // Set the uniform value to the one specified
        glUniform1iv(_loc, a_arr);
    }
}