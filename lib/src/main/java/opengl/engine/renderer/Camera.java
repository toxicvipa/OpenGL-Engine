package opengl.engine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

	private static final float tileSize = 32.0f;
	private static final float heightInTiles = 18.0f;
	private static final float widthInTiles = 32.0f;
	
	private Matrix4f projectionMatrix, viewMatrix;
	public Vector2f position;
	
	public Camera(Vector2f position) {
		this.position = position;
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		adjustProjection();
	}
	
	public void adjustProjection() {
		projectionMatrix.identity();
		projectionMatrix.ortho(0.0f, tileSize * widthInTiles, 0.0f, tileSize * heightInTiles, 0.0f, 100.0f);
	}
	
	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		viewMatrix.identity();
		viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
											cameraFront.add(position.x, position.y, 0.0f),
											cameraUp);
		return viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
}
