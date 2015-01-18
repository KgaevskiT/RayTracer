package material;

import java.awt.Color;

public class MattMaterial extends Material {
	public MattMaterial(Color color) {
		super(color, 0.0, 0.0, 0.0);
	}

	public MattMaterial(String texture) {
		super(texture, 0.0, 0.0, 0.0);
	}
}
