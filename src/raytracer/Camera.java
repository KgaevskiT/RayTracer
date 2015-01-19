package raytracer;

import geometry.Point3D;
import geometry.Vector3D;
import config.Config;

public class Camera {
	private Point3D position;
	private Vector3D direction;

	/* Distance reelle entre 2 pixels */
	private double delta;

	public Camera(Point3D position, Vector3D direction) {
		this.position = position;
		this.direction = direction;

		this.delta = 2 / (Math.min(Config.WIDTH, Config.HEIGHT));
	}

	public Camera(Point3D position, Point3D lookAt) {
		this.position = position;
		this.direction = new Vector3D(position, lookAt);
	}

	/**
	 * TODO: Compute camera angle and perspective.
	 * @param x X position of the final image.
	 * @param y Y position of the final image.
	 * @return
	 */
	public Ray getRay(double x, double y) {
		return new Ray(new Point3D(position.x + x, position.y + y,
				position.z), direction);
	}
}
