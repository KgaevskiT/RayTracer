package geometry.primitive;

public class Face {
	public int[] v = { -1, -1, -1, -1 };	// Vertices
	public int[] vt = { -1, -1, -1, -1 };	// Texture coordinates
	public int[] vn = { -1, -1, -1, -1 };	// Vertices normals
	private int size = 0;

	public void addVertex(String vertex) {
		String[] tokens = vertex.split("/");

		/* Vertex */
		v[size] = Integer.parseInt(tokens[0]) - 1;
		/* Texture */
		if (tokens.length >= 2)
			if (!tokens[1].equals(""))
				vt[size] = Integer.parseInt(tokens[1]) - 1;
		/* Normal */
		if (tokens.length >= 3)
			vn[size] = Integer.parseInt(tokens[2]) - 1;

		size++;
	}

	public int getSize() {
		return this.size;
	}
}
