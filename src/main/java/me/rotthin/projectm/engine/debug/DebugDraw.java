package me.rotthin.projectm.engine.debug;

import me.rotthin.projectm.engine.math.JMath;
import me.rotthin.projectm.engine.rendering.Shader;
import me.rotthin.projectm.engine.rendering.Window;
import me.rotthin.projectm.engine.utils.Constants;
import me.rotthin.projectm.engine.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public final class DebugDraw {
    private DebugDraw() {}

    public enum LifeTimeType {
        seconds,
        frames
    }

    private static final int MAX_LINES = 2000;
    private static final Shader SHADER = ResourceManager.getShader("assets/shaders/debugLine.glsl");

    private static float[] vertArray = new float[MAX_LINES * 6 * 2];
    private static List<Line> lines = new ArrayList<>(MAX_LINES);
    private static int vao, vbo;

    private static boolean started = false;

    public static void start(){
        started = true;

        // Generate the vao
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Create the vbo and buffer memory
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the vertex array attribs
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 5 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);
    }

    public static void update(float a_dt){
        if(!started) { start(); }

        for(int i=0; i<lines.size(); i++){
            lines.get(i).update(a_dt);
            if(lines.get(i).getLifeTime() < 0){
                lines.remove(i);

                i--;
            }
        }
    }

    public static void draw(){
        if(lines.size() <= 0) return;

        int _index = 0;
        for(Line _l : lines){
            Vector3f _color = _l.getColor();
            for(int i=0; i<2; i++){
                Vector2f _pos = i == 0 ? _l.getStart() : _l.getEnd();

                // Load pos
                vertArray[_index] = _pos.x;
                vertArray[_index+1] = _pos.y;

                // Load color
                vertArray[_index+2] = _color.x;
                vertArray[_index+3] = _color.y;
                vertArray[_index+4] = _color.z;

                _index += 5;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertArray, 0, lines.size()*5*2));

        // Use the shader
        SHADER.bind();
        SHADER.setMat4f("uProj", Window.getCurrentScene().getCamera().getProjectionMatrix());
        SHADER.setMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        // Bind the vao
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glLineWidth(2.5f);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size()*5*2);

        // Unbind the vao
        glBindVertexArray(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        SHADER.unbind();
    }

    public static void addLine(Vector2f a_start, Vector2f a_end){
        addLine(a_start, a_end, new Vector3f(0, 1, 0), 1, LifeTimeType.frames);
    }

    public static void addLine(Vector2f a_start, Vector2f a_end, Vector3f a_color){
        addLine(a_start, a_end, a_color, 1, LifeTimeType.frames);
    }

    public static void addLine(Vector2f a_start, Vector2f a_end, Vector3f a_color, float a_lifeTime, LifeTimeType a_type){
        while(lines.size() >= MAX_LINES){
            lines.remove(0);
        }

        lines.add(new Line(a_start, a_end, a_color, a_lifeTime, a_type));
    }

    public static void addBox(Vector2f a_center, Vector2f a_dims){
        addBox(a_center, a_dims, 0, Constants.COLOR3_GREEN, 1, LifeTimeType.frames);
    }

    public static void addBox(Vector2f a_center, Vector2f a_dims, float a_rotation){
        addBox(a_center, a_dims, a_rotation, Constants.COLOR3_GREEN, 1, LifeTimeType.frames);
    }

    public static void addBox(Vector2f a_center, Vector2f a_dims, Vector3f a_color){
        addBox(a_center, a_dims, 0, a_color, 1, LifeTimeType.frames);
    }

    public static void addBox(Vector2f a_center, Vector2f a_dims, float a_rotation, Vector3f a_color){
        addBox(a_center, a_dims, a_rotation, a_color, 1, LifeTimeType.frames);
    }

    public static void addBox(Vector2f a_center, Vector2f a_dims, float a_rotation, Vector3f a_color, float a_lifeTime, LifeTimeType a_type){
        Vector2f _min = new Vector2f(a_center).sub(new Vector2f(a_dims).mul(0.5f));
        Vector2f _max = new Vector2f(a_center).add(new Vector2f(a_dims).mul(0.5f));

        Vector2f[] _verts = {
            new Vector2f(_min.x, _min.y),   // Bottom left
            new Vector2f(_min.x, _max.y),   // Top left
            new Vector2f(_max.x, _max.y),   // Top right
            new Vector2f(_max.x, _min.y),   // Bottom right
        };

        if(a_rotation != 0){
            for(Vector2f _vert : _verts){
                JMath.rotate(_vert, a_rotation, a_center);
            }
        }

        addLine(_verts[0], _verts[1], a_color, a_lifeTime, a_type);
        addLine(_verts[0], _verts[3], a_color, a_lifeTime, a_type);
        addLine(_verts[1], _verts[2], a_color, a_lifeTime, a_type);
        addLine(_verts[2], _verts[3], a_color, a_lifeTime, a_type);
    }

    public static void addCircle(Vector2f a_center, float a_radius){
        addCircle(a_center, a_radius, Constants.COLOR3_GREEN, 1, LifeTimeType.frames);
    }

    public static void addCircle(Vector2f a_center, float a_radius, Vector3f a_color){
        addCircle(a_center, a_radius, a_color, 1, LifeTimeType.frames);
    }

    public static void addCircle(Vector2f a_center, float a_radius, float a_lifeTime, LifeTimeType a_type){
        addCircle(a_center, a_radius, Constants.COLOR3_GREEN, a_lifeTime, a_type);
    }

    public static void addCircle(Vector2f a_center, float a_radius, Vector3f a_color, float a_lifeTime, LifeTimeType a_type){
        Vector2f[] _points = new Vector2f[20];
        int _increment = 360 / _points.length;
        int _currentAngle = 0;

        Vector2f _tmp = new Vector2f();
        for(int i=0; i<_points.length; i++){
            _tmp.set(a_radius, 0);
            JMath.rotate(_tmp, _currentAngle, new Vector2f());
            _points[i] = new Vector2f(_tmp).add(a_center);

            if(i > 0){
                addLine(_points[i-1], _points[i], a_color, a_lifeTime, a_type);
            }

            _currentAngle += _increment;
        }

        addLine(_points[_points.length-1], _points[0], a_color, a_lifeTime, a_type);
    }
}
