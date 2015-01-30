package material;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Material {

	public static Material VOID = null;
	public static Material GLASS = new Material(new Color(220, 220, 220), 0.2, 0.6, 1.5);
	public static Material METAL = new Material(new Color(160, 160, 160), 0.5, 0.0, 0.0);
	public static Material WATER = new Material(new Color(100, 180, 255), 0.2, 0.4, 1.333);
	public static Material MIRROR = new Material(Color.BLACK, 1.0, 0.0, 0.0);
	//public static Material EARTH = new Material("textures/world.jpg", 0.0, 0.0, 0.0);
	//public static Material MOON = new Material("textures/moon.jpg", 0.0, 0.0, 0.0);
	//public static Material MARS = new Material("textures/mars.jpg", 0.0, 0.0, 0.0);
	public static Double DIAMOND = 2.4175;

	private Color color;
	private BufferedImage texture = null;
	private double reflectionRate;
	private double refractionRate;
	private double refractionIndex;

	public Material(Color color, double reflectionRate,	double refractionRate, double refractionIndex) {
		this.color = color;
		this.reflectionRate = reflectionRate;
		this.refractionRate = refractionRate;
		this.refractionIndex = refractionIndex;
	}

	public Material(String texture, double reflectionRate, double refractionRate, double refractionIndex) {
		try {
			this.texture = ImageIO.read(new File(texture));
		} catch (IOException e) {
			System.err.println("Error: Could not read texture file (" + texture + ")");
			this.color = Color.white;
			this.texture = null;
		}
		this.reflectionRate = reflectionRate;
		this.refractionRate = refractionRate;
		this.refractionIndex = refractionIndex;
	}

	public Color getColor() {
		return color;
	}

	public BufferedImage getTexture() {
		return texture;
	}

	public double getReflectionRate() {
		return reflectionRate;
	}

	public double getRefractionRate() {
		return refractionRate;
	}

	public double getRefractionIndex() {
		return refractionIndex;
	}
}
