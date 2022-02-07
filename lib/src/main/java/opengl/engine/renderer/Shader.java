package opengl.engine.renderer;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
	
	private int shaderProgramID;
	
	private String vertexSource;
	private String fragmentSource;
	private String filepath;

	public Shader(String filepath) {
		this.filepath = filepath;
		try {
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] split = source.split("(#type)( )+([a-zA-Z]+)");
			
			int index = source.indexOf("#type") + 6;
			int eol = source.indexOf("\r\n", index);
			String firstPattern = source.substring(index, eol).trim();
			
			index = source.indexOf("#type", eol) + 6;
			eol = source.indexOf("\r\n", index);
			String secondPattern = source.substring(index, eol).trim();
			
			if (firstPattern.equals("vertex")) {
				vertexSource = split[1];
			} else if (firstPattern.equals("fragment")) {
				fragmentSource = split[1];
			} else {
				throw new IOException("Unexpected token: " + firstPattern);
			}
			
			if (secondPattern.equals("vertex")) {
				vertexSource = split[2];
			} else if (secondPattern.equals("fragment")) {
				fragmentSource = split[2];
			} else {
				throw new IOException("Unexpected token: " + secondPattern);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error opening shader: " + filepath);
			System.exit(1);
		}
	}
	
	public void compile() {
        int vertexID, fragmentID;

        // First load and compile the vertex shader
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        GL20.glShaderSource(vertexID, vertexSource);
        GL20.glCompileShader(vertexID);

        // Check for errors in compilation
        int success = GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS);
        if (success == GL11.GL_FALSE) {
            int len = GL20.glGetShaderi(vertexID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(GL20.glGetShaderInfoLog(vertexID, len));
            System.exit(1);
        }

        // First load and compile the vertex shader
        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        GL20.glShaderSource(fragmentID, fragmentSource);
        GL20.glCompileShader(fragmentID);

        // Check for errors in compilation
        success = GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS);
        if (success == GL11.GL_FALSE) {
            int len = GL20.glGetShaderi(fragmentID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed.");
            System.out.println(GL20.glGetShaderInfoLog(fragmentID, len));
            System.exit(1);
        }

        // Link shaders and check for errors
        shaderProgramID = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgramID, vertexID);
        GL20.glAttachShader(shaderProgramID, fragmentID);
        GL20.glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = GL20.glGetProgrami(shaderProgramID, GL20.GL_LINK_STATUS);
        if (success == GL11.GL_FALSE) {
            int len = GL20.glGetProgrami(shaderProgramID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed.");
            System.out.println(GL20.glGetProgramInfoLog(shaderProgramID, len));
            System.exit(1);
        }
	}
	
	public void use() {
		GL20.glUseProgram(shaderProgramID);
	}
	
	public void detach() {
		GL20.glUseProgram(0);
	}
	
	public void uploadMat4f(String varName, Matrix4f mat4) {
		int varLocation = GL20.glGetUniformLocation(shaderProgramID, varName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		GL20.glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	
}
