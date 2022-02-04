package opengl.engine.display;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import opengl.engine.io.KeyListener;
import opengl.engine.io.MouseListener;

public class Window {

	private int width, height;
	private String title;
	private long glfwWindow;
	
	private static Window INSTANCE;
	
	private Window() {
		width = 1920;
		height = 1200;
		title = "Engine";
	}
	
	public void run() {
		System.out.println("LWJGL Version: " + Version.getVersion() + "!");
		
		init();
		loop();
		
		// Free memory
		Callbacks.glfwFreeCallbacks(glfwWindow);
		GLFW.glfwDestroyWindow(glfwWindow);
		
		// Terminate GLFW
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	
	private void init() {
		// Error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		// Configure GLFW
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
		
		// Create window
		glfwWindow = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(glfwWindow == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to create window");
		}
		
		GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		GLFW.glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		// Make the context current
		GLFW.glfwMakeContextCurrent(glfwWindow);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);
		// Show window
		GLFW.glfwShowWindow(glfwWindow);
		// Important
		GL.createCapabilities();
	}
	
	private void loop() {
		while(!GLFW.glfwWindowShouldClose(glfwWindow)) {
			// Poll events
			GLFW.glfwPollEvents();
			
			GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			GLFW.glfwSwapBuffers(glfwWindow);
		}
	}
	
	public static Window getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Window();
		}
		return INSTANCE;
	}
	
}
