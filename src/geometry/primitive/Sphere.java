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
	private Double radius;

	public Sphere(Point3D center, Double radius, Material material) {
		super(material);
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Intersection intersect(Ray ray) {
		Point3D E = ray.getOrigin();
		Vector3D V = ray.getDirection();
		Vector3D EO = new Vector3D(E, this.center, false);

		Double v = EO.dotProduct(ray.getDirection());
		Double d = radius * radius - (EO.dotProduct(EO) - (v * v));

		if (d <= 0.0)
			return null;
		else {
			d = Math.sqrt(d);
			if (v - d <= 0)
				return null;
		}

		Point3D p = new Point3D(E.x + (v - d) * V.x, E.y + (v - d) * V.y, E.z + (v - d) * V.z);

		return new Intersection(this, p, v - d);
	}

	@Override
	public Double getCosine(Ray ray) {
		Vector3D n = new Vector3D(this.center, ray.getOrigin());

		return n.dotProduct(ray.getDirection());
	}

	@Override
	public Ray getReflected(Ray ray, Point3D p) {
		Vector3D normal = new Vector3D(this.center, p);
		Vector3D direction = ray.getDirection();

		Double c1 = - normal.dotProduct(direction);

		Vector3D reflectedDir = new Vector3D(direction.x + (2 * normal.x * c1),
				direction.y + (2 * normal.y * c1), direction.z + (2 * normal.z * c1));

		Ray reflected = new Ray(p, reflectedDir);

		return reflected;
	}

	@Override
	public Ray getRefracted(Ray ray, Point3D p) {
		Double oRefIndex = ray.getRefractionIndex();
		Double newRefIndex = this.material.getRefractionIndex();

		Double n = oRefIndex / newRefIndex;

		Vector3D normal = new Vector3D(this.center, p);
		Vector3D direction = ray.getDirection();

		Double c1 = - normal.dotProduct(direction);
		Double c2 = Math.sqrt(1 - n * n * Math.pow(1 - c1, 2));

		Vector3D refractedDir = new Vector3D((n * direction.x) + (n * c1 - c2) * normal.x,
				(n * direction.y) + (n * c1 - c2) * normal.y,
				(n * direction.z) + (n * c1 - c2) * normal.z);

		Ray refracted = new Ray(p, refractedDir, newRefIndex);

		return refracted;
	}

	@Override
	public Color getColor(Point3D p) {
		if (this.material.getColor() != null)
			return this.material.getColor();

		// North pole
		Vector3D vn = new Vector3D(0.0, 0.1, 0.0);
		// Equator
		Vector3D ve = new Vector3D(1.0, 0.0, 0.0);
		// Position
		Vector3D vp = new Vector3D(this.center, p);

		// Horizontal pixel position
		Double u;
		// Vertical pixel position
		Double v;

		Double phi = Math.acos(-vn.dotProduct(vp));
		v = phi / Math.PI;

		Double theta = (Math.acos(ve.dotProduct(vp) / Math.sin(phi))) / (2 * Math.PI);
		if (vn.crossProduct(ve).dotProduct(vp) > 0)
			u = theta;
		else
			u = 1 - theta;

		BufferedImage texture = material.getTexture();

		int h = (int) (v * texture.getHeight());
		int w = (int) (u * texture.getWidth());

		h = h == 0 ? 1 : h;
		w = w == 0 ? 1 : w;

		return new Color(texture.getRGB(texture.getWidth() - w, texture.getHeight() - h));
	}

	public Double getRadius() {
		return this.radius;
	}
}
