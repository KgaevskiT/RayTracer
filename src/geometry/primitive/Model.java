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

	private Intersection intersectTriangle(Ray ray, int faceId, Point3D vert1,
			Point3D vert2, Point3D vert3) {

		/* Ray data */
		Vector3D dir = ray.getDirection();
		Point3D orig = ray.getOrigin();

		// get triangle edge vectors and plane normal
		Vector3D u = Vector3D.sub(vert2, vert1);
		Vector3D v = Vector3D.sub(vert3, vert1);
		Vector3D n = Vector3D.cross(u, v);

		Vector3D w0 = Vector3D.sub(orig, vert1);
		double a = - Vector3D.dot(n, w0);
		double b = Vector3D.dot(n, dir);

		if (Math.abs(b) < EPSILON) {     // ray is  parallel to triangle plane
			if (a == 0)                 // ray lies in triangle plane
				return null;
			else
				return null;              // ray disjoint from plane
		}

		// get intersect point of ray with triangle plane
		double r = a / b;
		if (r < EPSILON)                    // ray goes away from triangle
			return null;                   // => no intersect
		// for a segment, also test if (r > 1.0) => no intersect

		Point3D I = new Point3D(orig.x + r * dir.x, orig.y + r * dir.y, orig.z + r * dir.z);            // intersect point of ray and plane

		// is I inside T?
		double uu = Vector3D.dot(u, u);
		double uv = Vector3D.dot(u, v);
		double vv = Vector3D.dot(v, v);
		Vector3D w = Vector3D.sub(I, vert1);
		double wu = Vector3D.dot(w, u);
		double wv = Vector3D.dot(w,v);
		double D = uv * uv - uu * vv;

		// get and test parametric coords
		double s = (uv * wv - vv * wu) / D;
		if (s < 0.0 || s > 1.0)         // I is outside T
			return null;
		double t = (uv * wu - uu * wv) / D;
		if (t < 0.0 || (s + t) > 1.0)  // I is outside T
			return null;

		return new Intersection(this, faceId, I, r); // I is in T
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

		if (normals.isEmpty())
			return faceNormal;
		else {

			Vector3D vertexNormal;
			if (triangle.getSize() == 3)
				vertexNormal = Vector3D.mean(normals.get(triangle.vn[0]),
						normals.get(triangle.vn[1]),
						normals.get(triangle.vn[2]));
			else
				vertexNormal = Vector3D.mean(normals.get(triangle.vn[0]),
						normals.get(triangle.vn[1]),
						normals.get(triangle.vn[2]),
						normals.get(triangle.vn[3]));

			double dot = Vector3D.dot(faceNormal, vertexNormal);

			if (dot < 0.0) {
				faceNormal.inverse();
				return faceNormal;
			}
			else
				return faceNormal;
		}
	}

	private void setHitBox() {
		int maxX = 0, minX = 0, maxY = 0, minY = 0, maxZ = 0, minZ = 0;
		for (int i = 1; i < vertices.size(); i++) {
			if (vertices.get(i).x > vertices.get(maxX).x)
				maxX = i;
			if (vertices.get(i).x < vertices.get(minX).x)
				minX = i;
			if (vertices.get(i).y > vertices.get(maxY).y)
				maxY = i;
			if (vertices.get(i).y < vertices.get(minY).y)
				minY = i;
			if (vertices.get(i).z > vertices.get(maxZ).z)
				maxZ = i;
			if (vertices.get(i).z < vertices.get(minZ).z)
				minZ = i;
		}
		Point3D center = Point3D.barycenter(new Point3D[] {
				vertices.get(maxX), vertices.get(minX),	vertices.get(maxY),
				vertices.get(minY),	vertices.get(maxZ), vertices.get(minZ) });

		double radius = 0;
		double distance = center.distance(vertices.get(maxX));
		if (distance > radius)
			radius = distance;
		distance = center.distance(vertices.get(minX));
		if (distance > radius)
			radius = distance;
		distance = center.distance(vertices.get(maxY));
		if (distance > radius)
			radius = distance;
		distance = center.distance(vertices.get(minY));
		if (distance > radius)
			radius = distance;
		distance = center.distance(vertices.get(maxZ));
		if (distance > radius)
			radius = distance;
		distance = center.distance(vertices.get(minZ));
		if (distance > radius)
			radius = distance;

		hitbox = new Sphere(center, radius, Material.VOID);
	}
}
