package opengl.engine.display.scene;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import opengl.engine.renderer.Shader;

public class SplashscreenScene extends Scene {

    private float[] vertexArray = {
        // position               // color
         200f, 0.0f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
        0.0f,  200f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
         200f,  200f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
        0.0f, 0.0f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x        x
                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
	
	@Override
	public void init() {
		camera.position.x = -10;
		camera.position.y = -10;
        defaultShader = new Shader("src/main/resources/shaders/default.glsl");
        defaultShader.compile();

        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, vboID);
        GL20.glBufferData(GL20.GL_ARRAY_BUFFER, vertexBuffer, GL20.GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = GL20.glGenBuffers();
        GL20.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, eboID);
        GL20.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL20.GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        GL20.glVertexAttribPointer(0, positionsSize, GL20.GL_FLOAT, false, vertexSizeBytes, 0);
        GL20.glEnableVertexAttribArray(0);

        GL20.glVertexAttribPointer(1, colorSize, GL20.GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        GL20.glEnableVertexAttribArray(1);
	}

	@Override
	public void tick(float dt) {
		camera.position.x -= dt * 128.0f;
		camera.position.y -= dt * 128.0f;
	}

	@Override
	public void render() {
		defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        // Bind the VAO that we're using
        GL30.glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glDrawElements(GL30.GL_TRIANGLES, elementArray.length, GL30.GL_UNSIGNED_INT, 0);

        // Unbind everything
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);

        defaultShader.detach();
	}

}
