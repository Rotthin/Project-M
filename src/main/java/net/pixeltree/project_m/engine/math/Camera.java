package net.pixeltree.project_m.engine.math;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f a_pos){
        position = a_pos;

        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity();

        // Set the orthographic projection
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 50.0f);
    }

    public Matrix4f getViewMatrix(){
        Vector3f _cameraFront = new Vector3f(0, 0, -1);
        Vector3f _cameraUp = new Vector3f(0, 1, 0);

        viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), _cameraFront.add(position.x, position.y, 0.0f), _cameraUp);

        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
