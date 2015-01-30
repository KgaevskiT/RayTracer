package geometry.primitive;
import geometry.Intersection;
import geometry.Point3D;
import geometry.Vector3D;

import java.awt.Color;
import java.awt.image.BufferedImage;

import material.Material;
import raytracer.Ray;


public class Sphere extends Primitive {
	private Point3D center;
	private double radius;

	public Sphere(Point3D center, double radius, Material material) {
		super(material);
		this.center = center;
		this.radius = radius;
	}

	public Sphere(Point3D center, double radius, Material material, String name) {
		super(material, name);
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Intersection intersect(Ray ray) {
		Point3D O = ray.getOrigin();		// Ray origin E
		Vector3D D = ray.getDirection();
		Vector3D OC = new Vector3D(O, this.center, false);

		double v = Vector3D.dot(OC, D);
		double d = Math.pow(radius, 2) - (Vector3D.dot(OC, OC) - Math.pow(v, 2));

		if (d <= EPSILON)
			return null;
		else {
			d = Math.sqrt(d);
			if (v - d <= EPSILON)
				return null;
		}

		Point3D p = new Point3D(O.x + (v - d) * D.x, O.y + (v - d) * D.y, O.z + (v - d) * D.z);

		return new Intersection(this, p, v - d);
	}

	@Override
	public double getCosine(Ray ray, Intersection intersection) {
		Vector3D normal = getNormal(intersection);

		if (ray.isInObject())
			normal.inverse();

		return Vector3D.dot(ray.getDirection(), normal);
	}

	@Override
	public Vector3D getNormal(Intersection intersection) {
		return new Vector3D(this.center, intersection.getPoint());
	}

	@Override
	public Color getColor(Intersection intersection) {
		/* Uniform color */
		if (this.material.getColor() != null)
			return this.material.getColor();

		/* Texture */

		// North pole
		Vector3D vn = new Vector3D(0, 1, 0); // Vector3D.Y;
		// Equator
		Vector3D ve = new Vector3D(1, 0, 0); // Vector3D.X;
		// Position
		Vector3D vp = new Vector3D(this.center, intersection.getPoint());

		// Horizontal pixel position
		double u;
		// Vertical pixel position
		double v;

		double phi = Math.acos(-Vector3D.dot(vn, vp));
		v = phi / Math.PI;

		double theta = (Math.acos(Vector3D.dot(ve, vp) / Math.sin(phi))) / (2 * Math.PI);
		if (Vector3D.dot(Vector3D.cross(vn, ve), vp) > 0)
			u = theta;
		else
			u = 1 - theta;

		BufferedImage texture = material.getTexture();

		int h = (int) (v * texture.getHeight());
		int w = (int) (u * texture.getWidth());

		h = h == 0 ? 1 : h;
		h = h == texture.getHeight() ? texture.getHeight() - 1 : h;
		w = w == 0 ? 1 : w;
		w = w == texture.getWidth() ? texture.getWidth() - 1 : w;

		return new Color(texture.getRGB(w, texture.getHeight() - h));
	}

	public double getRadius() {
		return this.radius;
	}

	public void move(Vector3D v) {
		center.move(v);
	}
}
