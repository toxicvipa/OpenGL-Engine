package opengl.engine.io;

import org.lwjgl.glfw.GLFW;

public class MouseListener {
	
	private static MouseListener INSTANCE;
	
	private double scrollX, scrollY;
	private double x, y, lastX, lastY;
	private boolean buttons[] = new boolean[3];
	private boolean dragging;
	
	private MouseListener() {
		scrollX = 0.0;
		scrollY = 0.0;
		x = 0.0;
		y = 0.0;
		lastX = 0.0;
		lastY = 0.0;
	}
	
	public static void mousePosCallback(long window, double x, double y) {
		MouseListener instance = getInstance();
		instance.lastX = instance.x;
		instance.lastY = instance.y;
		instance.x = x;
		instance.y = y;
		instance.dragging = instance.buttons[0] || instance.buttons[1] || instance.buttons[2];
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		MouseListener instance = getInstance();
		if(button >= instance.buttons.length || button < 0) {
			return;
		}
		if(!(instance.buttons[button] = action == GLFW.GLFW_PRESS)) {
			instance.dragging = false;
		}
	}
	
	public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
		MouseListener instance = getInstance();
		instance.scrollX = xOffset;
		instance.scrollY = yOffset;
	}
	
	public static void endFrame() {
		MouseListener instance = getInstance();
		instance.scrollX = 0;
		instance.scrollY = 0;
		instance.lastX = instance.x;
		instance.lastY = instance.y;
	}
	
	public float getX() {
		return (float) getInstance().x;
	}	
	
	public float getY() {
		return (float) getInstance().y;
	}
	
	public float getDx() {
		MouseListener instance = getInstance();
		return (float) (instance.lastX - instance.x);
	}
	
	public float getDy() {
		MouseListener instance = getInstance();
		return (float) (instance.lastY - instance.y);
	}
	
	public float getScrollY() {
		return (float) getInstance().scrollY;
	}
	
	public float getScrollX() {
		return (float) getInstance().scrollX;
	}
	
	public boolean isDragging() {
		return getInstance().dragging;
	}
	
	public boolean isPressed(int button) {
		MouseListener instance = getInstance();
		return button < instance.buttons.length && button >= 0 && instance.buttons[button];
	}
	
	public static MouseListener getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new MouseListener();
		}
		return INSTANCE;
	}

}
