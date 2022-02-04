package opengl.engine.io;

import org.lwjgl.glfw.GLFW;

public class KeyListener {

	private static KeyListener INSTANCE;
	
	private boolean keys[] = new boolean[512];
	
	private KeyListener() {
		
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		KeyListener instance = getInstance();
		if(key >= instance.keys.length || key < 0) {
			return;
		}
		instance.keys[key] = action != GLFW.GLFW_RELEASE;
	}
	
	public boolean isPressed(int key) {
		KeyListener instance = getInstance();
		return key < instance.keys.length && key >= 0 && instance.keys[key];
	}
	
	public static KeyListener getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new KeyListener();
		}
		return INSTANCE;
	}
	
}
