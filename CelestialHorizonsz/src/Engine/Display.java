package Engine;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

/**
 * This class contains all the methods needed to set-up, maintain, and close a
 * LWJGL display.
 * 
 * @author Karl
 *
 */
public class Display {

	public long windowID;

	@SuppressWarnings("unused")
	private boolean vsync;
	private int WIDTH = 1280;
	private int HEIGHT = 720;
	private int FPS_CAP = 60;
	private boolean resized;
	private String TITLE = "Celestial Horizons";

	public Display(int width, int height, String title, boolean vsync) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.TITLE = title;
		this.vsync = vsync;
		this.resized = false;
	}

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		windowID = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		
		
		// Setup resize callback
		glfwSetFramebufferSizeCallback(windowID, (window, width, height) -> {
			this.WIDTH = width;
			this.HEIGHT = height;
			this.setResized(true);
		});

		glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true); // We will detect this
														// in the rendering loop
			}
		});
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(windowID, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2

		);
		
		
		
		
		
		
		
		
		
		glfwMakeContextCurrent(windowID);
		glfwShowWindow(windowID);
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
	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(windowID, keyCode) == GLFW_PRESS;
	}
	public void setClearColor(float r, float g, float b, float alpha) {
		glClearColor(r, g, b, alpha);
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
	public boolean isResized() {
		return resized;
	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}
}