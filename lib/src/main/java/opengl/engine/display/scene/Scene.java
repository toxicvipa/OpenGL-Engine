package opengl.engine.display.scene;

public abstract class Scene {
	
	public Scene() {
		
	}
	
	public abstract void tick(float dt);
	public abstract void render();

}
