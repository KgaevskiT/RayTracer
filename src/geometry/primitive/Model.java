package geometry.primitive;

import geometry.Intersection;
import geometry.Point3D;
import geometry.Vector3D;

import java.awt.Color;
import java.util.ArrayList;

import material.Material;
import material.MattMaterial;
import raytracer.Ray;

/**
 * Complex model (from OBJ file)
 * @author Thomas
 *
 */
public class Model extends Primitive {
	private String objectName;
	private ArrayList<Point3D> vertices = new ArrayList<Point3D>();
	private ArrayList<Vector3D> normals = new ArrayList<Vector3D>();
	private ArrayList<Face> faces = new ArrayList<Face>();
	private Sphere hitbox = null;

	public Model(String objectName, ArrayList<Point3D> vertices,
			ArrayList<Vector3D> normals, ArrayList<Face> faces) {
		super(new MattMaterial(Color.RED));
		this.objectName = objectName;
		this.vertices = vertices;
		this.normals = normals;
		this.faces = faces;
		setHitBox();
	}

	/**
	 * Homothety from 0.
	 * @param ratio Parameter of the homothety.
	 */
	public void resize(double ratio) {
		for (Point3D p : vertices) {
			p.x = p.x * ratio;
			p.y = p.y * ratio;
			p.z = p.z * ratio;
		}
	}

	@Override
	public Intersection intersect(Ray ray) {
		if (hitbox.intersect(ray) == null)
			return null;

		Intersection closest = null;

		for (int i = 0; i < faces.size(); i++) {
			Face f = faces.get(i);

			// TODO Verifier que les points sont dans l'ordre dans le fichier
			Intersection intersection = intersectTriangle(ray, i,
					vertices.get(f.v[0]), vertices.get(f.v[1]), vertices.get(f.v[2]));
			closest = getClosest(closest, intersection);
			if (f.getSize() == 4) {
				Intersection intersection2 = intersectTriangle(ray, i,
						vertices.get(f.v[0]), vertices.get(f.v[2]), vertices.get(f.v[3]));
				closest = getClosest(closest, intersection2);
			}
		}
		return closest;
	}

	/**
	 * Calculate the closest intersection from two.
	 * @param intersection1
	 * @param intersection2
	 * @return The closest intersection between intersection1 and intersection2.
	 */
	private Intersection getClosest(Intersection intersection1, Intersection intersection2) {
		if (intersection1 == null && intersection2 == null)
			return null;
		if (intersection1 == null)
			return intersection2;
		if (intersection2 == null)
			return intersection1;
		if (intersection1.getDistance() < intersection2.getDistance())
			return intersection1;
		else
			return intersection2;
	}

	/**
	 * Moller-Trumbore intersection algorithm: Fast method for calculating the
	 * intersection of a ray and a triangle in 3 dimensions without needing
	 * precomputation of the plane equation of the plane containing the triangle
	 * @param ray
	 * @param faceId
	 * @param vert1
	 * @param vert2
	 * @param vert3
	 * @return The intersection between the ray and the triangle, or null if
	 * they does not intersect
	 */
	private Intersection MollerTrumbore(Ray ray, int faceId, Point3D vert1,
			Point3D vert2, Point3D vert3) {

		/* Ray data */
		Vector3D dir = ray.getDirection();
		Point3D orig = ray.getOrigin();

		/* Find vectors for two edges sharing vert1 */
		Vector3D edge1 = Vector3D.sub(vert2, vert1);
		Vector3D edge2 = Vector3D.sub(vert3, vert1);

		/* Begin calculating determinant - also used to calculate U parameter */
		Vector3D P = Vector3D.cross(dir, edge2);
		double det = Vector3D.dot(edge1, P);

		/* If determinant is near zero, ray lies in plan of triangle */
		if (det > -EPSILON && det < EPSILON)
			return null;

		/* Calculate distance from v1 to ray origin */
		Vector3D T = Vector3D.sub(orig, vert1);

		/* Calculate U parameter and test bounds */
		double inv_det = 1. / det;
		double u = Vector3D.dot(T, P) * inv_det;
		if (u < 0. || u > 1.)
			return null;

		/* Prepare to test V parameter */
		Vector3D Q = Vector3D.cross(T, edge1);

		/* Calculate V parameter and test bounds */
		double v = Vector3D.dot(dir, Q) * inv_det;
		if (v < 0. || v > 1. || u + v > 1.)
			return null;

		/* Calculate t, scale parameters, ray intersects triangle */
		double t = Vector3D.dot(edge2, Q) * inv_det;

		System.out.println(u + " " + v + " " + t);

		/* Ray intersection */
		//if (t > EPSILON) {
		// Point3D point = new Point3D(orig.x + t * dir.x, orig.y + t * dir.y, orig.z + t * dir.z);
		Point3D point = new Point3D((1 - u - v) * vert1.x + u * vert2.x + v * vert3.x,
				(1 - u - v) * vert1.y + u * vert2.y + v * vert3.y,
				(1 - u - v) * vert1.z + u * vert2.z + v * vert3.z);
		return new Intersection(this, faceId, point, t);
		//}

		/* No intersection */
		//return null;
	}

	/**
	 * Calculate the intersection between a ray and a triangle.
	 * @param ray
	 * @param faceId ID of the face
	 * @param vert1
	 * @param vert2
	 * @param vert3
	 * @return
	 */
	private Intersection intersectTriangle(Ray ray, int faceId, Point3D vert1,
			Point3D vert2, Point3D vert3) {

		/* Ray data */
		Vector3D dir = ray.getDirection();
		Point3D orig = ray.getOrigin();

		/* Triangle edge and plane normal */
		Vector3D edge1 = Vector3D.sub(vert2, vert1);
		Vector3D edge2 = Vector3D.sub(vert3, vert1);
		Vector3D normal = Vector3D.cross(edge1, edge2);

		Vector3D w0 = Vector3D.sub(orig, vert1);
		double a = - Vector3D.dot(normal, w0);
		double b = Vector3D.dot(normal, dir);

		/* Ray is parallel or on the triangle plane */
		if (Math.abs(b) < EPSILON)
			return null;

		/* Get intersect point of ray with triangle plane */
		double r = a / b;
		if (r < EPSILON)
			return null;
		Point3D point = new Point3D(orig.x + r * dir.x, orig.y + r * dir.y, orig.z + r * dir.z);            // intersect point of ray and plane

		/* Calculate if the point is in the triangle */
		double uu = Vector3D.dot(edge1, edge1);
		double uv = Vector3D.dot(edge1, edge2);
		double vv = Vector3D.dot(edge2, edge2);
		Vector3D w = Vector3D.sub(point, vert1);
		double wu = Vector3D.dot(w, edge1);
		double wv = Vector3D.dot(w,edge2);
		double D = uv * uv - uu * vv;

		// Get and test parametric coordinates */
		double s = (uv * wv - vv * wu) / D;
		if (s < 0.0 || s > 1.0)
			return null;
		double t = (uv * wu - uu * wv) / D;
		if (t < 0.0 || (s + t) > 1.0)
			return null;

		return new Intersection(this, faceId, point, r);
	}

	@Override
	public Color getColor(Intersection intersection) {
		if (this.material.getColor() != null)
			return this.material.getColor();
		else
			System.err.println("Error: Textured model not implemented");
		return null;
	}

	@Override
	public Vector3D getNormal(Intersection intersection) {
		Face triangle = faces.get(intersection.getFaceId());
		/* Triangle vertices */
		Point3D v1 = vertices.get(triangle.v[0]);
		Point3D v2 = vertices.get(triangle.v[1]);
		Point3D v3 = vertices.get(triangle.v[2]);
		/* Triangle edges */
		Vector3D e1 = new Vector3D(v1, v2);
		Vector3D e2 = new Vector3D(v1, v3);

		Vector3D faceNormal = Vector3D.cross(e1, e2);

		/* If normals are defined, check the face normal direction */
		if (normals.isEmpty())
			return faceNormal;
		else {
			Vector3D[] vertexNormals = new Vector3D[triangle.getSize()];
			for (int i = 0; i < triangle.getSize(); i++)
				vertexNormals[i] = normals.get(triangle.vn[i]);

			Vector3D vertexNormal = Vector3D.mean(vertexNormals);
			double dot = Vector3D.dot(faceNormal, vertexNormal);

			if (dot < 0.0) {
				faceNormal.inverse();
				return faceNormal;
			}
			else
				return faceNormal;
		}
	}

	/**
	 * Set the model hitbox.
	 * The hitbox is a sphere having the model barycenter as center, and the
	 * farrest vertex distance as radius.
	 */
	private void setHitBox() {
		double x = 0, y = 0, z = 0;
		for (Point3D p : vertices) {
			x += p.x;
			y += p.y;
			z += p.z;
		}

		Point3D center = new Point3D(x / vertices.size(),
				y / vertices.size(), z / vertices.size());

		double radius = 0;
		for (Point3D p : vertices) {
			double distance = center.distance(p);
			if (distance > radius)
				radius = distance;
		}

		hitbox = new Sphere(center, radius, Material.VOID);
	}
}
