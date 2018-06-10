package Engine;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.opengl.GLCapabilities;

import org.lwjgl.opengl.GL;

/**
 * This class contains all the methods needed to set-up, maintain, and close a
 * LWJGL display.
 * 
 * @author Karl
 *
 */
public class Display {

	public long windowID;

	private boolean vsync;
	private int WIDTH = 1280;
	private int HEIGHT = 720;
	private int FPS_CAP = 60;
	private String TITLE = "Our First Display";

	public Display(int width, int height, String title, boolean vsync) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.TITLE = title;
		this.vsync = vsync;

	}

	public void init() {
		windowID = glfwCreateWindow(WIDTH, HEIGHT, TITLE, glfwGetPrimaryMonitor(), NULL);
		
		glfwMakeContextCurrent(windowID);
        GLCapabilities cap = GL.createCapabilities();
        GL.setCapabilities(cap);
        
        
	}

	public void updateDisplay() {
		sync(FPS_CAP);

	}

	/**
	 * This closes the window when the game is closed.
	 */
	public void closeDisplay() {
		glfwDestroyWindow(windowID);
	}

	private long variableYieldTime, lastTime;

	/**
	 * An accurate sync method that adapts automatically to the system it runs on to
	 * provide reliable results.
	 * 
	 * @param fps
	 *            The desired frame rate, in frames per second
	 * @author kappa (On the LWJGL Forums)
	 */
	private void sync(int fps) {
		if (fps <= 0)
			return;

		long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
		// yieldTime + remainder micro & nano seconds if smaller than sleepTime
		long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000 * 1000));
		long overSleep = 0; // time the sync goes over by

		try {
			while (true) {
				long t = System.nanoTime() - lastTime;

				if (t < sleepTime - yieldTime) {
					Thread.sleep(1);
				} else if (t < sleepTime) {
					// burn the last few CPU cycles to ensure accuracy
					Thread.yield();
				} else {
					overSleep = t - sleepTime;
					break; // exit while loop
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);

			// auto tune the time sync should yield
			if (overSleep > variableYieldTime) {
				// increasze by 200 microseconds (1/5 a ms)
				variableYieldTime = Math.min(variableYieldTime + 200 * 1000, sleepTime);
			} else if (overSleep < variableYieldTime - 200 * 1000) {
				// decrease by 2 microseconds
				variableYieldTime = Math.max(variableYieldTime - 2 * 1000, 0);
			}
		}
	}

	public long getWindowID() {
		return windowID;
	}

}