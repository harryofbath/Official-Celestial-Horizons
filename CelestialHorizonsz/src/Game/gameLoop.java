package Game;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.lwjgl.opengl.GL;

import Engine.Display;
import Engine.Load;
import Engine.RawModel;
import Engine.Renderer;

public class gameLoop {

	public static void main(String[] args) {
		glfwInit();
		gameLoop loop = new gameLoop();
		
		loop.loop();
		
	}

	public void loop() {
		
		boolean vsync = true;
		int WIDTH = 1920;
		int HEIGHT = 1080;
		int FPS_CAP = 60;
		String TITLE = "Our First Display";

		Display display = new Display(WIDTH, HEIGHT, TITLE, vsync);
		
		
		
		
		display.init();
		
		
		
		

		Load loader = new Load();
		Renderer renderer = new Renderer();

		boolean glfwCheck = glfwInit();
		if (!glfwCheck) {
			throw new IllegalStateException("GLFW initialization failed");

		}

		float[] vertices = {
				// Left bottom triangle
				-0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f,
				// Right top triangle
				0.5f, -0.5f, 0f, 0.5f, 0.5f, 0f, -0.5f, 0.5f, 0f };


		RawModel model = loader.loadToVAO(vertices);

		while (!glfwWindowShouldClose(display.getWindowID())) {

			renderer.prepare();
			renderer.render(model);
			display.updateDisplay();
		}

		loader.cleanUp();
		display.closeDisplay();

	}

}