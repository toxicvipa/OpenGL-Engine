package opengl.engine.util;

public class Time {

	public static final float timeStarted = System.nanoTime();
	
	public static float getTime() {
		return (float) ((System.nanoTime() - timeStarted) * 1E-9);
	}
	
}
