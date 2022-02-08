package opengl.engine.renderer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

public class Texture {
	
	private int texID;
	
	public Texture(String filepath) {
		
		texID = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
		
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = STBImage.stbi_load(filepath, width, height, channels, 0);
		
		if(image != null) {
			if(channels.get(0) == 3) {
				GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGB, width.get(0), height.get(0), 0, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE, image);
			} else if(channels.get(0) == 4){
				GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width.get(0), height.get(0), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, image);
			} else {
				throw new RuntimeException("Unknown number of channels: " + channels);
			}
		} else {
			throw new RuntimeException("Could not load image: " +  filepath);
		}
		
		STBImage.stbi_image_free(image);
	}
	
	public void bind() {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);
	}
	
	public void unbind() {
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
	}

}
