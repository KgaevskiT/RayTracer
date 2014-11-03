package material;

import java.awt.Color;

public class ReflectiveMaterial extends Material {
	public ReflectiveMaterial(Color color, double reflectionRate) {
		super(color, reflectionRate, 0.0, 0.0);
	}
}
