package raytracer;

import geometry.Point3D;
import geometry.Vector3D;
import config.Config;

public class Camera {
	/* Frame of the Camera space */
	private Point3D origin;	// Camera position
	private Vector3D U;		// Direction of the "look at" point
	private Vector3D V;		// Direction for the top
	private Vector3D W;		// Third direction

	private double focal;	// Focal length
	private double delta = (double) 2 / Math.min(Config.HEIGHT, Config.WIDTH);	// Size of a pixel

	/**
	 * Set the top by default to the natural top (0, 1, 0)
	 * @param position Position of the camera.
	 * @param lookAt Center of the render image.
	 * @param fieldOfView Angle of view.
	 */
	public Camera(Point3D position, Point3D lookAt, double fieldOfView) {
		this.origin = position;
		this.U = new Vector3D(position, lookAt);
		this.V = new Vector3D(0, 1, 0);
		this.W = Vector3D.cross(V, U);
		this.W.normalize();
		this.focal = 2;//1 / Math.tan(fieldOfView / 2);
	}

	/**
	 * @param position Position of the camera.
	 * @param lookAt Center of the render image.
	 * @param up Approximative direction of the top.
	 * @param fieldOfView Angle of view.
	 */
	public Camera(Point3D position, Point3D lookAt, Vector3D up, double fieldOfView) {
		this.origin = position;
		this.U = new Vector3D(position, lookAt);
		this.V = Vector3D.sub(up, Vector3D.mul(Vector3D.dot(U, up), U));
		this.W = Vector3D.cross(V, U);
		this.focal = 1 / Math.tan(fieldOfView / 2);
	}

	/**
	 * @param i Vertical position (height).
	 * @param j Horizontal position (width).
	 * @return The ray from the camera to the scene for the given pixel.
	 */
	public Ray getRay(int i, int j) {
		Vector3D u = Vector3D.mul(focal, U);
		Vector3D v = Vector3D.mul(delta * (Config.HEIGHT / 2 - i), V);
		Vector3D w = Vector3D.mul(delta * (Config.WIDTH / 2 - j), W);
		Vector3D direction = Vector3D.add(new Vector3D[] { u, v, w });
		direction.normalize();
		return new Ray(origin, direction);
	}
}
