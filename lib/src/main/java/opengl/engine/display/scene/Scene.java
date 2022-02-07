package opengl.engine.display.scene;

import org.joml.Vector2f;

import opengl.engine.renderer.Camera;

public abstract class Scene {
	
	protected Camera camera;
	
	public Scene() {
		camera = new Camera(new Vector2f());
	}
	
	public abstract void init();
	public abstract void tick(float dt);
	public abstract void render();

}
