package Shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

import Engine.Load;

public class ShaderClass {
	
	int programID;
	int vertexID;
	int fragmentID;

	public void attachFragment(String id) {
		
		
		
		String fragmentShaderSource = Load.readFile(id);

		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentShaderSource);

		glCompileShader(fragmentID);

		if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("An error occured when creating the fragment shader. \n"
					+ glGetShaderInfoLog(fragmentID, glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH)));

		}
		
		glAttachShader(programID, fragmentID);

	}
	
	public void attachVertex(String id) {
		String vertexShaderSource = Load.readFile(id);

		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexShaderSource);

		glCompileShader(vertexID);

		if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("An error occured when creating the fragment shader. \n"
					+ glGetShaderInfoLog(vertexID, glGetShaderi(vertexID, GL_INFO_LOG_LENGTH)));

		}
		
		glAttachShader(programID, vertexID);

	}
}
