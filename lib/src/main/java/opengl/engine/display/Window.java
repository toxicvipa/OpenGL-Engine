package opengl.engine.display;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import opengl.engine.display.scene.SceneManager;
import opengl.engine.display.scene.SplashscreenScene;
import opengl.engine.io.KeyListener;
import opengl.engine.io.MouseListener;
import opengl.engine.util.Time;

public class Window {

	private int width, height;
	private String title;
	private long glfwWindow;
	private float TPS = 30.0f;
	private float FPS = 60.0f;
	
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
		GLFW.glfwSetWindowSizeCallback(glfwWindow, this::windowResizeCallback);
		GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		
		// Make the context current
		GLFW.glfwMakeContextCurrent(glfwWindow);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);
		// Show window
		GLFW.glfwShowWindow(glfwWindow);
		// Important
		GL.createCapabilities();
		// Enable Alpha
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND );
	}
	
	private void windowResizeCallback(long window, int width, int height) {
		GL11.glViewport(0, 0, width, height);
		GL11.glScissor(0, 0, width, height);
	}
	
	private void loop() {
		float startTime = Time.getTime();
		float endTime = Time.getTime();
		float timer = 0.0f;
		float lastTick = Time.getTime();
		int tickCount = 0;
		int renderCount = 0;
		while(!GLFW.glfwWindowShouldClose(glfwWindow)) {
			// Poll events
			GLFW.glfwPollEvents();
			
			GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			if(tickCount * (1.0f/TPS) < timer) {
				float tickTime = Time.getTime();
				SceneManager.getInstance().tick(tickTime - lastTick);
				lastTick = tickTime;
				tickCount++;
			}
			if(renderCount * (1.0f/FPS) < timer) {
				SceneManager.getInstance().render();
				renderCount++;
			}
			
			if(MouseListener.getInstance().isPressed(1)) {
				SceneManager.getInstance().setScene(new SplashscreenScene());
			}
			
			GLFW.glfwSwapBuffers(glfwWindow);
			
			endTime = Time.getTime();
			timer += endTime - startTime;
			if(timer > 1.0f) {
				timer -= 1.0f;
				tickCount = 0;
				renderCount = 0;
			}
			startTime = endTime;
		}
	}
	
	public static Window getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Window();
		}
		return INSTANCE;
	}
	
}
