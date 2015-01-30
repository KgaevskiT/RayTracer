package geometry.primitive;
import geometry.Intersection;
import geometry.Vector3D;

import java.awt.Color;

import material.Material;
import raytracer.Ray;


public abstract class Primitive {
	protected static double EPSILON = 0.001;	// Double precision error (meaning "near zero")

	protected String name;
	protected Material material;

	public Primitive(Material material) {
		this.material = material;
	}

	public Primitive(Material material, String name) {
		this.material = material;
		this.name = name;
	}

	public abstract Vector3D getNormal(Intersection intersection);
	public abstract Intersection intersect(Ray ray);
	public abstract Color getColor(Intersection intersection);

	public double getCosine(Ray ray, Intersection intersection) {
		return Vector3D.dot(getNormal(intersection), ray.getDirection());
	}

	public String getName() {
		return name;
	}

	public Ray getReflected(Ray ray, Intersection intersection) {
		Vector3D normal = intersection.getNormal(this);
		Vector3D direction = ray.getDirection();

		double c1 = - Vector3D.dot(normal, direction);

		Vector3D reflectedDir = new Vector3D(direction.x + (2 * normal.x * c1),
				direction.y + (2 * normal.y * c1), direction.z + (2 * normal.z * c1));

		Ray reflected = new Ray(intersection.getPoint(), reflectedDir);

		return reflected;
	}

	public Ray getRefracted(Ray ray, Intersection intersection) {
		double oldRefIndex = ray.getRefractionIndex();
		double newRefIndex;
		boolean isInObject = ray.isInObject();

		if (isInObject)
			newRefIndex = 1.0;
		else
			newRefIndex = this.material.getRefractionIndex();

		double n = oldRefIndex / newRefIndex;

		Vector3D normal = intersection.getNormal(this);
		Vector3D direction = ray.getDirection();

		double c1 = - Vector3D.dot(normal, direction);
		double c2 = Math.sqrt(1 - n * n * Math.pow(1 - c1, 2));

		Vector3D refractedDir = new Vector3D((n * direction.x) + (n * c1 - c2) * normal.x,
				(n * direction.y) + (n * c1 - c2) * normal.y,
				(n * direction.z) + (n * c1 - c2) * normal.z);

		Ray refracted = new Ray(intersection.getPoint(), refractedDir, newRefIndex, !isInObject);

		return refracted;
	}

	public double getReflectionRate() {
		return this.material.getReflectionRate();
	}

	public double getRefractionRate() {
		return this.material.getRefractionRate();
	}
}
