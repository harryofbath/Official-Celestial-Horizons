package Engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Load {

	private ByteList indice = new ByteList();

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();

	private int indicesCount;

	private int vboID = GL15.glGenBuffers();

	public int getvboiID() {
		return vboID;
	}

	public int getIndicesCount() {
		return indicesCount;

	}

	public RawModel loadToVAO(float[] positions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 3);
	}

	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, float[] data) {

		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, flipByteBuffer(indice.indices), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	public ByteBuffer flipByteBuffer(byte[] indices) {
		// OpenGL expects to draw vertices in counter clockwise order by default

		int indicesCount = indices.length;
		this.indicesCount = indicesCount;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		return indicesBuffer;
	}

	public static String readFile(String filename) {
		StringBuilder source = new StringBuilder();
		try {
			File file = new File(filename);
			String path = file.getAbsolutePath();

			FileInputStream fis = new FileInputStream(path);
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(fis));
			@SuppressWarnings("unused")
			String line;
			while ((line = reader.readLine()) != null) {
				source.append("\n");

			}

			reader.close();

		} catch (Exception e) {
			System.err.println("Error using name: " + filename);
			e.printStackTrace();
			System.exit(-1);

		}

		return source.toString();
	}

	public static String[] viewAll(String name) {
		return readFile(name).split("\n");

	}

}
