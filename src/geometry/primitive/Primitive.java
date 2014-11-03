package geometry.primitive;
import geometry.Intersection;
import geometry.Point3D;

import java.awt.Color;

import material.Material;
import raytracer.Ray;


public abstract class Primitive {
	protected Material material;

	public Primitive(Material material) {
		this.material = material;
	}

	public abstract Intersection intersect(Ray ray);
	public abstract Color getColor(Point3D p);
	public abstract Double getCosine(Ray ray);
	public abstract Ray getReflected(Ray ray, Point3D p);
	public abstract Ray getRefracted(Ray ray, Point3D p);

	public double getReflectionRate() {
		return this.material.getReflectionRate();
	}

	public double getRefractionRate() {
		return this.material.getRefractionRate();
	}
}
