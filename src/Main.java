import geometry.Point3D;
import geometry.primitive.Light;
import geometry.primitive.Scene;
import geometry.primitive.Sphere;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import material.Material;
import material.MatteMaterial;
import material.ReflectiveMaterial;
import raytracer.Camera;
import raytracer.RayTracer;


public class Main {

	public static void main(String[] args) {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -1000.0), new Point3D(0.0, 0.0, 0.0)));
		//scene.addPrimitive(new Plane(new Point3D(0.0, 0.0, 0.0), new Vector3D(0.0, 1.0, 0.0), Color.BLUE));
		//scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 0.0), 800.0, "world_relief.jpg"));
		//scene.addPrimitive(new Sphere(new Point3D(150.0, -50.0, 80.0), 80.0, Color.RED));
		//scene.addPrimitive(new Sphere(new Point3D(-200.0, 50.0, 0.0), 50.0, Color.ORANGE));
		//scene.addLight(new Light(new Point3D(1000.0, 1000.0, -1000.0), 50));
		//scene.addLight(new Light(new Point3D(300.0, -100.0, 200.0), 100));

		BufferedImage image = new RayTracer(getReflect()).render();

		try {
			ImageIO.write(image, "png", new File("image.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Scene getSimple() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -300.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 0.0), 100.0, new MatteMaterial(Color.RED)));
		scene.addLight(new Light(new Point3D(100.0, 100.0, -300.0), 50));
		return scene;
	}

	private static Scene getScene() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -300.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(-50.0, 0.0, -20.0), 100.0, new MatteMaterial(Color.GREEN)));
		scene.addPrimitive(new Sphere(new Point3D(150.0, -50.0, 80.0), 80.0, new MatteMaterial(Color.RED)));
		scene.addPrimitive(new Sphere(new Point3D(-200.0, 50.0, 0.0), 50.0, new MatteMaterial(Color.BLUE)));
		scene.addPrimitive(new Sphere(new Point3D(-100.0, 170.0, -100.0), 20.0, new MatteMaterial(Color.ORANGE)));
		scene.addPrimitive(new Sphere(new Point3D(250.0, -100.0, 300.0), 200.0, new MatteMaterial(Color.PINK)));
		scene.addPrimitive(new Sphere(new Point3D(-220.0, -100.0, 0.0), 60.0, new MatteMaterial(Color.CYAN)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 1000.0), 250.0, new MatteMaterial(Color.GRAY)));
		scene.addLight(new Light(new Point3D(-100.0, 100.0, -300.0), 50));
		return scene;
	}

	private static Scene getReflect() {
		Scene scene = new Scene(new Camera(new Point3D(0.0, 0.0, -300.0), new Point3D(0.0, 0.0, 0.0)));
		scene.addPrimitive(new Sphere(new Point3D(-50.0, 0.0, -20.0), 100.0, new ReflectiveMaterial(Color.ORANGE, 0.5)));
		scene.addPrimitive(new Sphere(new Point3D(150.0, -50.0, 80.0), 80.0, new MatteMaterial(Color.RED)));
		scene.addPrimitive(new Sphere(new Point3D(-200.0, 50.0, 0.0), 50.0, Material.WATER));
		scene.addPrimitive(new Sphere(new Point3D(-100.0, 170.0, -100.0), 20.0, Material.METAL));
		scene.addPrimitive(new Sphere(new Point3D(250.0, -100.0, 300.0), 200.0, new MatteMaterial(Color.GREEN)));
		scene.addPrimitive(new Sphere(new Point3D(-220.0, -100.0, 0.0), 60.0, new MatteMaterial(Color.CYAN)));
		scene.addPrimitive(new Sphere(new Point3D(0.0, 0.0, 1000.0), 250.0, Material.MIRROR));
		scene.addLight(new Light(new Point3D(-100.0, 100.0, -300.0), 50));
		return scene;
	}
}
