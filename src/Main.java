import geometry.Point3D;
import geometry.Vector3D;
import geometry.primitive.Light;
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
		RayTracer rayTracer = new RayTracer(getTableLamp());
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
		table.move(new Vector3D(0, -100, 0));
		scene.addPrimitive(lamp);
		scene.addPrimitive(table);
		scene.addLight(new Light(new Point3D(30, 30, 30), 50));
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
		Scene scene = new Scene(new Camera(new Point3D(-1, 2, -2), Point3D.ORIGIN, 90));
		WaveFront cube = new WaveFront("obj/cube.obj");
		scene.addPrimitive(cube);
		scene.addLight(new Light(new Point3D(2, 2, -2), 50));
		return scene;
	}

	private static Scene getCubeSphere() {
		Scene scene = new Scene(new Camera(new Point3D(-2, 2, -5), Point3D.ORIGIN, 90));
		WaveFront cube = new WaveFront("obj/cube.obj");
		Sphere sphere = new Sphere(new Point3D(-1, 0, 1), 1, new MattMaterial(Color.BLUE));
		scene.addPrimitive(cube);
		scene.addPrimitive(sphere);
		scene.addLight(new Light(new Point3D(0, 2, -2), 50));
		return scene;
	}

	private static Scene getEarth() {
		Scene scene = new Scene(new Camera(new Point3D(0, 0, -1000), Point3D.ORIGIN, 90));
		scene.addPrimitive(new Sphere(new Point3D(0, 0, 0), 800, new MattMaterial("textures/mars.jpg")));
		scene.addLight(new Light(new Point3D(500, 500, -2000), 50));
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
		scene.addPrimitive(new Sphere(new Point3D(0, 0, 10), 2.5, Material.MIRROR));
		scene.addLight(new Light(new Point3D(-1, 1, -3), 50));
		return scene;
	}
}
