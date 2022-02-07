package opengl.engine.display.scene;

import org.lwjgl.opengl.GL11;

public class SceneManager {
	
	private static SceneManager INSTANCE;
	
	private Scene currentScene = new SplashscreenScene();
	private Scene newScene = null;
	
	private boolean changingScene = false;
	private float secondsForTransition = 1.0f;
	private float secondsBlack = 0.5f;
	private float transitionProgress = 0.0f;
	private float fadeAlpha = 0.0f;
	private boolean initialized = false;
	
	private SceneManager() {
		currentScene.init();
	}
	
	public void tick(float dt) {
		if(changingScene) {
			transitionProgress += dt;
			if(transitionProgress >= secondsForTransition + secondsBlack) {
				changingScene = false;
				transitionProgress = 0.0f;
				initialized = false;
				return;
			}
			if(transitionProgress <= secondsForTransition / 2) {
				fadeAlpha = 1.0f - Math.abs(transitionProgress - secondsForTransition / 2f) / (secondsForTransition / 2f);
			} else if(transitionProgress <= secondsForTransition / 2 + secondsBlack) {
				if(!initialized) {
					currentScene = newScene;
					newScene = null;
					currentScene.init();
					initialized = true;
				}
				fadeAlpha = 1.0f;
			} else {
				fadeAlpha = 1.0f - Math.abs(transitionProgress - secondsBlack - secondsForTransition / 2f) / (secondsForTransition / 2f);
			}
		} else {
			currentScene.tick(dt);
		}
	}
	
	public void render() {
		currentScene.render();
		GL11.glColor4f(0.0f, 0.0f, 0.0f, fadeAlpha);
		GL11.glRectf(-1f, -1f, 1f, 1f);
	}
	
	public void setScene(Scene newScene) {
		if(changingScene) {
			return;
		}
		this.newScene = newScene;
		changingScene =  true;
		System.out.println("Change scene: " +  newScene.getClass().getName() + " => " + currentScene.getClass().getName());
	}
	
	public static SceneManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new SceneManager();
		}
		return INSTANCE;
	}

}
