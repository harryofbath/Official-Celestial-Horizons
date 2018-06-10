package Engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class Renderer {

	private ByteList bytelist = new ByteList();
	byte[] indice = bytelist.indices;
	
	

	Load load = new Load();

	
	
	
	public void prepare() {
		glClearColor(1, 0, 0, 1);
		glClear(GL_COLOR_BUFFER_BIT);
	}

	public void render(RawModel model) {
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, load.getvboiID());
		glDrawElements(GL11.GL_TRIANGLES, load.getIndicesCount(), GL_UNSIGNED_BYTE, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		
		
	

	}
	


}
