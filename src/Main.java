import geometry.Point3D;
import geometry.Vector3D;
import geometry.primitive.Light;
import geometry.primitive.Plane;
import geometry.primitive.Scene;
import geometry.primitive.Sphere;
import geometry.primitive.WaveFront;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

import javax.imageio.ImageIO;

import material.BrightMaterial;
import material.Material;
import material.MattMaterial;
import raytracer.Camera;
import raytracer.RayTracer;


public class Main {

	public static void main(String[] args) {
		ForkJoinPool pool = new ForkJoinPool();
		RayTracer rayTracer = new RayTracer(getMini());
		BufferedImage image = pool.invoke(rayTracer);

		try {
			ImageIO.write(image, "png", new File("image.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Scene getDucky() {
		Scene scene = new Scene(new Camera(new Point3D(500, 500, 500), Point3D.ORIGIN, 90));
		WaveFront ducky = new WaveFront("obj/Ducky.obj");
		scene.addPrimitive(ducky);
		scene.addLight(new Light(new Point3D(300, 0, 300), 50));
		return scene;
	}

	private static Scene getTableLamp() {
		Scene scene = new Scene(new Camera(new Point3D(0, 20, 200), new Vector3D(0, 0, -1), 90));
		WaveFront lamp = new WaveFront("obj/lamp.obj");
		WaveFront table = new WaveFront("obj/table.obj");
		table.move(new Vector3D(0, -100, 0, false));
		scene.addPrimitive(lamp);
		scene.addPrimitive(table);
		scene.addLight(new Light(new Point3D(30, 30, 30), 50));
		return scene;
	}

	private static Scene getMini() {
		Scene scene = new Scene(new Camera(new Point3D(300, 300, 300), Point3D.ORIGIN, 90));
		WaveFront mini = new WaveFront("obj/mini.obj");
		scene.addPrimitive(mini);
		scene.addLight(new Light(new Point3D(200, 400, 600), 50));
		return scene;
	}

	private static Scene getToyplane() {
		Scene scene = new Scene(new Camera(new Point3D(40, 10, 30), Point3D.ORIGIN, 90));
		scene.addPrimitive(new WaveFront("obj/toyplane.obj"));
		scene.addPrimitive(new Plane(new Point3D(0, -6, 0), Vector3D.Y, new MattMaterial(Color.GRAY)));
		scene.addLight(new Light(new Point3D(20, 50, 50), 50));
		return scene;
	}

	private static Scene getPorsche() {
		Scene scene = new Scene(new Camera(new Point3D(2, 2, -5), Point3D.ORIGIN, 90));
		WaveFront porsche = new WaveFront("obj/porsche/Porsche_911_GT2.obj");
		scene.addPrimitive(porsche);
		scene.addLight(new Light(new Point3D(2, 2, 2), 50));
		return scene;
	}

	private static Scene getCube() {
		Scene scene = new Scene(new Camera(new Point3D(1.5, 2, 3), Point3D.ORIGIN, 90));
		WaveFront cube = new WaveFront("obj/cube.obj");
		scene.addPrimitive(cube);
		scene.addLight(new Light(new Point3D(2, 3, 4), 50));
		return scene;
	}

	private static Scene getCubeSphere() {
		Scene scene = new Scene(new Camera(new Point3D(3, 3, 5), Point3D.ORIGIN, 90));
		scene.addPrimitive(new WaveFront("obj/cube.obj"));
		scene.addPrimitive(new Sphere(new Point3D(0.5, 0, 2), 0.5, new MattMaterial(Color.BLUE), "sphere"));
		scene.addPrimitive(new Sphere(new Point3D(0.5, 1, -2), 1, new MattMaterial(Color.GREEN), "sphere"));
		scene.addLight(new Light(new Point3D(0.5, 0.5, 5), 50));
		return scene;
	}

	private static Scene getEarth() {
		Scene scene = new Scene(new Camera(new Point3D(-30, 0, 0), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Sphere(Point3D.ORIGIN, 10, new MattMaterial("textures/world.jpg")));
		scene.addLight(new Light(new Point3D(-20, 20, -30), 50));
		return scene;
	}

	private static Scene getSolarSystemReal() {
		Scene scene = new Scene(new Camera(new Point3D(227902, 0, 4), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Sphere(Point3D.ORIGIN, 1391.684, new MattMaterial("textures/sun.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(57900, 0, 0), 2.4397, new MattMaterial("textures/mercury_color.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(108200, 0, 0), 6.0518, new MattMaterial("textures/venus.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(149500, 0, 0), 6.378, new MattMaterial("textures/world.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(227900, 0, 0), 3.3962, new MattMaterial("textures/mars.jpg")));
		scene.addLight(new Light(new Point3D(-20, 20, -30), 50));
		return scene;
	}

	private static Scene getSolarSystem() {
		//Scene scene = new Scene(new Camera(new Point3D(100000, 0, 300000), new Point3D(100000, 0, 0), 90));
		Scene scene = new Scene(new Camera(new Point3D(149508, 3, 6), new Point3D(0, 0, 0), 90));
		scene.addPrimitive(new Sphere(new Point3D(0, 0, 0), 1391.684, new MattMaterial("textures/sun.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(57900, 0, 0), 2.4397, new MattMaterial("textures/mercury_color.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(108200, 0, 0), 6.0518, new MattMaterial("textures/venus.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(149500, 0, 0), 6.378, new MattMaterial("textures/world.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(149200, 0, 50), 1.7374, new MattMaterial("textures/moon_color.jpg")));
		scene.addPrimitive(new Sphere(new Point3D(227900, 0, 0), 3.3962, new MattMaterial("textures/mars.jpg")));
		//		scene.addLight(new Light(new Point3D(20000, 0, 20000), 50));
		//		scene.addLight(new Light(new Point3D(-20000, 0, 20000), 50));
		//		scene.addLight(new Light(new Point3D(0, 20000, 20000), 50));
		//		scene.addLight(new Light(new Point3D(0, -20000, 20000), 50));
		//		scene.addLight(new Light(new Point3D(0, 0, 30000), 50));
		scene.addLight(new Light(new Point3D(30000, 0, 0), 50));
		scene.addLight(new Light(new Point3D(20000, 0, 20000), 50));
		scene.addLight(new Light(new Point3D(20000, 0, -20000), 50));
		scene.addLight(new Light(new Point3D(20000, 20000, 0), 50));
		scene.addLight(new Light(new Point3D(20000, -20000, 0), 50));
		scene.addLight(new Light(new Point3D(149200, 0, 0), 50));
		scene.addLight(new Light(new Point3D(149492, 4, 8), 50));
		return scene;
	}

	private static Scene getBasic() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, -10), Point3D.ORIGIN, Math.PI));
		scene.addPrimitive(new Sphere(new Point3D(0, -10, 0), 10, new MattMaterial(Color.RED)));
		scene.addPrimitive(new Sphere(new Point3D(1, 3, -1), 1, new BrightMaterial(Color.BLUE, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(-2, 1, -1), 1, new BrightMaterial(Color.GREEN, 0.5)));
		scene.addLight(new Light(new Point3D(0, 10, 0), 50));
		scene.addLight(new Light(new Point3D(3, -1, -15), 100));
		return scene;
	}

	private static Scene getSimple() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, 10), Point3D.ORIGIN, Math.PI / 2));
		scene.addPrimitive(new Sphere(new Point3D(0, 0, 0), 1, new MattMaterial(Color.RED)));
		scene.addLight(new Light(new Point3D(2, 2, 2), 50));
		return scene;
	}

	private static Scene getScene() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, -8), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Sphere(new Point3D(-0.5, 0, -0.2), 1, new MattMaterial(Color.GREEN)));
		scene.addPrimitive(new Sphere(new Point3D(1.5, -0.5, 0.8), 0.8, new MattMaterial(Color.RED)));
		scene.addPrimitive(new Sphere(new Point3D(-2, 0.5, 0), 0.5, new MattMaterial(Color.BLUE)));
		scene.addPrimitive(new Sphere(new Point3D(-1, 1.7, -1), 0.2, new MattMaterial(Color.ORANGE)));
		scene.addPrimitive(new Sphere(new Point3D(2.5, -1, 3), 2, new MattMaterial(Color.PINK)));
		scene.addPrimitive(new Sphere(new Point3D(-2.2, -1, 0), 0.6, new MattMaterial(Color.CYAN)));
		scene.addPrimitive(new Sphere(new Point3D(0, 0, 10), 2.5, new MattMaterial(Color.GRAY)));
		scene.addLight(new Light(new Point3D(-1, 1, -3), 50));
		return scene;
	}

	private static Scene getReflect() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, -8), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Sphere(new Point3D(-0.5, 0, -0.2), 1, new BrightMaterial(Color.ORANGE, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(1.5, -0.5, 0.8), 0.8, new BrightMaterial(Color.RED, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(-2, 0.5, 0), 0.5, Material.WATER));
		scene.addPrimitive(new Sphere(new Point3D(-1, 1.7, -1), 0.2, Material.METAL));
		scene.addPrimitive(new Sphere(new Point3D(2.5, -1, 3), 2, new BrightMaterial(Color.GREEN, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(-2.2, -1, 0), 0.6, new BrightMaterial(Color.CYAN, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(5, 5, 5), 4, Material.MIRROR));
		scene.addPrimitive(new Plane(new Point3D(0, -2, 0), Vector3D.Y, new MattMaterial(Color.GRAY)));
		scene.addLight(new Light(new Point3D(-1, 1, -3), 50));
		return scene;
	}

	private static Scene getRoom() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, 10), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Plane(new Point3D(0, -5, 0), Vector3D.Y, new MattMaterial(new Color(255, 127, 127))));
		//scene.addPrimitive(new Plane(new Point3D(0, 5, 0), Vector3D.Y, new MattMaterial(Color.BLUE)));
		//scene.addPrimitive(new Plane(new Point3D(-5, 0, 0), Vector3D.X, new MattMaterial(Color.GREEN)));
		//scene.addPrimitive(new Plane(new Point3D(5, 0, 0), Vector3D.X, new MattMaterial(Color.ORANGE)));
		scene.addPrimitive(new Plane(new Point3D(0, 0, -10), Vector3D.Z, new MattMaterial(new Color(255, 127, 127))));
		scene.addPrimitive(new Sphere(Point3D.ORIGIN, 2, new Material(Color.WHITE, 0, 1, 2.5), "transparente"));
		scene.addPrimitive(new Sphere(new Point3D(-2, 2, -4), 2, new Material(Color.BLUE, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(-2, -2, -4), 2, new Material(Color.RED, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(2, 2, -4), 2, new Material(Color.GREEN, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(2, -2, -4), 2, new Material(Color.YELLOW, 0, 0, 0)));
		scene.addLight(new Light(new Point3D(1, 1, 5), 50));
		return scene;
	}

	private static Scene getRefraction() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, 10), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Sphere(Point3D.ORIGIN, 3, new Material(Color.WHITE, 0.1, 0.9, 1.2), "transparente"));
		scene.addPrimitive(new Sphere(new Point3D(-2, 3, -6), 3, new Material(Color.BLUE, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(-2, -1, -5), 1, new Material(Color.RED, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(3, 3, -8), 3, new Material(Color.GREEN, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(2, -2, -5), 2, new Material(Color.ORANGE, 0, 0, 0)));
		//scene.addPrimitive(new Sphere(new Point3D(0, 2, -8), 3, new Material(Color.RED, 0, 0, 0)));
		scene.addLight(new Light(new Point3D(1, 1, 5), 50));
		return scene;
	}

	private static Scene getMirror() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, 10), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Plane(new Point3D(0, -5, 0), Vector3D.Y, new MattMaterial(new Color(255, 127, 127))));
		scene.addPrimitive(new Plane(new Point3D(0, 0, -10), Vector3D.Z, new MattMaterial(new Color(255, 127, 127))));
		scene.addPrimitive(new Sphere(new Point3D(0, 0, -4), 3, Material.MIRROR));
		scene.addPrimitive(new Sphere(new Point3D(-2, 2, 0), 1, new Material(Color.BLUE, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(-2, -2, 0), 1, new Material(Color.RED, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(2, 2, 0), 1, new Material(Color.GREEN, 0, 0, 0)));
		scene.addPrimitive(new Sphere(new Point3D(2, -2, 0), 1, new Material(Color.YELLOW, 0, 0, 0)));
		scene.addLight(new Light(new Point3D(1, 1, 5), 50));
		return scene;
	}
}
