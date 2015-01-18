import geometry.Point3D;
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
		RayTracer rayTracer = new RayTracer(getReflect());
		BufferedImage image = pool.invoke(rayTracer);

		try {
			ImageIO.write(image, "png", new File("image.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Scene getDucky() {
		Scene scene = new Scene(new Camera(new Point3D(0., 0., 1000.), new Point3D(0., 0., 0.)));
		WaveFront ducky = new WaveFront("obj/Ducky.obj");
		scene.addPrimitive(ducky);
		scene.addLight(new Light(new Point3D(100., 0., 100.), 50));
		return scene;
	}

	private static Scene getPorsche() {
		Scene scene = new Scene(new Camera(new Point3D(1000., 0., 0.), new Point3D(0., 0., 0.)));
		WaveFront porsche = new WaveFront("obj/porsche/Porsche_911_GT2.obj");
		//porsche.resize(100);
		scene.addPrimitive(porsche);
		scene.addLight(new Light(new Point3D(200., 100., -500.), 50));
		return scene;
	}

	private static Scene getCube() {
		Scene scene = new Scene(new Camera(new Point3D(0., 0., -1.), new Point3D(0., 0., 0.)));
		WaveFront cube = new WaveFront("obj/cube.obj");
		cube.resize(100);
		scene.addPrimitive(cube);
		scene.addLight(new Light(new Point3D(0., 0.0, -50.), 50));
		return scene;
	}

	private static Scene getCubeSphere() {
		Scene scene = new Scene(new Camera(new Point3D(10.0, 10.0, -10.0), new Point3D(0.0, 0.0, 0.0)));
		WaveFront cube = new WaveFront("obj/cube.obj");
		Sphere sphere = new Sphere(new Point3D(300., 0., 100.), 100., new MattMaterial(Color.BLUE));
		cube.resize(100);
		scene.addPrimitive(cube);
		scene.addPrimitive(sphere);
		scene.addLight(new Light(new Point3D(150.0, 300.0, -100.0), 50));
		return scene;
	}

	private static Scene getEarth() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -1000.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 0.0), 800.0, new MattMaterial("textures/mars.jpg")));
		scene.addLight(new Light(new Point3D(500.0, 500.0, -2000.0), 50));
		return scene;
	}

	private static Scene getBasic() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -1000.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, -1000.0, 0.0), 1000.0, new MattMaterial(Color.RED)));
		scene.addPrimitive(new Sphere(new Point3D(100.0, 300.0, -100.0), 100.0, new BrightMaterial(Color.BLUE, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(-200.0, 100.0, -100.0), 100.0, new BrightMaterial(Color.GREEN, 0.5)));
		scene.addLight(new Light(new Point3D(0.0, 1000.0, 0.0), 50));
		scene.addLight(new Light(new Point3D(300.0, -100.0, -1500.0), 100));
		return scene;
	}

	private static Scene getSimple() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -1000.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 0.0), 500.0, new MattMaterial(Color.RED)));
		scene.addLight(new Light(new Point3D(400.0, 500.0, -1000.0), 50));
		return scene;
	}

	private static Scene getScene() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -300.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(-50.0, 0.0, -20.0), 100.0, new MattMaterial(Color.GREEN)));
		scene.addPrimitive(new Sphere(new Point3D(150.0, -50.0, 80.0), 80.0, new MattMaterial(Color.RED)));
		scene.addPrimitive(new Sphere(new Point3D(-200.0, 50.0, 0.0), 50.0, new MattMaterial(Color.BLUE)));
		scene.addPrimitive(new Sphere(new Point3D(-100.0, 170.0, -100.0), 20.0, new MattMaterial(Color.ORANGE)));
		scene.addPrimitive(new Sphere(new Point3D(250.0, -100.0, 300.0), 200.0, new MattMaterial(Color.PINK)));
		scene.addPrimitive(new Sphere(new Point3D(-220.0, -100.0, 0.0), 60.0, new MattMaterial(Color.CYAN)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 1000.0), 250.0, new MattMaterial(Color.GRAY)));
		scene.addLight(new Light(new Point3D(-100.0, 100.0, -300.0), 50));
		return scene;
	}

	private static Scene getReflect() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -300.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(-50.0, 0.0, -20.0), 100.0, new BrightMaterial(Color.ORANGE, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(150.0, -50.0, 80.0), 80.0, new MattMaterial(Color.RED)));
		scene.addPrimitive(new Sphere(new Point3D(-200.0, 50.0, 0.0), 50.0, Material.WATER));
		scene.addPrimitive(new Sphere(new Point3D(-100.0, 170.0, -100.0), 20.0, Material.METAL));
		scene.addPrimitive(new Sphere(new Point3D(250.0, -100.0, 300.0), 200.0, new MattMaterial(Color.GREEN)));
		scene.addPrimitive(new Sphere(new Point3D(-220.0, -100.0, 0.0), 60.0, new MattMaterial(Color.CYAN)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 1000.0), 250.0, Material.MIRROR));
		scene.addLight(new Light(new Point3D(-100.0, 100.0, -300.0), 50));
		return scene;
	}
}
