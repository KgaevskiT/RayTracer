package geometry.primitive;
import java.util.ArrayList;

import raytracer.Camera;


public class Scene {
	private ArrayList<Primitive> primitives;
	private ArrayList<Light> lights;
	private Camera camera;

	public Scene(Camera camera) {
		this.primitives = new ArrayList<Primitive>();
		this.lights = new ArrayList<Light>();
		this.camera = camera;
	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public void addPrimitive(Primitive primitive) {
		this.primitives.add(primitive);
	}

	public Camera getCamera() {
		return camera;
	}

	public ArrayList<Light> getLights() {
		return this.lights;
	}

	public ArrayList<Primitive> getPrimitives() {
		return this.primitives;
	}
}
