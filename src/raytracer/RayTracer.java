package raytracer;
import geometry.Intersection;
import geometry.Vector3D;
import geometry.primitive.Light;
import geometry.primitive.Primitive;
import geometry.primitive.Scene;

import java.awt.Color;
import java.awt.image.BufferedImage;

import postprocessing.AntiAliasing;
import utils.ProgressBar;
import config.Config;


public class RayTracer {
	private Scene scene;

	public RayTracer(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Compute the render image of the scene.
	 * @return Render image.
	 */
	public BufferedImage render() {
		BufferedImage image = new BufferedImage(Config.WIDTH, Config.HEIGHT, BufferedImage.TYPE_INT_RGB);

		/* Compute color for each pixel */
		for (int h = 0; h < Config.HEIGHT; h++) {
			for (int w = 0; w < Config.WIDTH; w++) {

				/* Ray from camera to the scene */
				Ray ray = scene.getCamera().getRay((double) (w - Config.WIDTH / 2),
						(double) (Config.HEIGHT - h - Config.HEIGHT / 2));

				/* Color of the pixel */
				Color color = traceRay(ray, 0);
				image.setRGB(w, h, color.getRGB());

				/* Just print progression */
				ProgressBar.print(h);
			}
		}

		ProgressBar.print(Config.HEIGHT);

		/* Post processing */
		if (Config.ANTI_ALIASING)
			return new AntiAliasing(image).gradientBlur();
		else
			return image;
	}

	/**
	 * Recursively compute the color of a pixel.
	 * @param ray The original ray comming from the camera.
	 * @param nbReflect Current number of reflections or refractions made.
	 * @return The color of the pixel.
	 */
	public Color traceRay(Ray ray, int nbReflect) {

		/* Stop recursion if maximum number of reflections if reached */
		if (nbReflect >= Config.MAX_REFLEXION)
			return null;

		/* Diffusion light : Color of the object */
		Color pointColor = null;
		/* Color from the reflected ray */
		Color reflectColor = null;
		/* Color from the refracted ray */
		Color refractColor = null;

		/* Compute first intersection from the camera */
		Intersection intersection = getFirstIntersection(ray);

		double reflectionRate = 0.0;
		double refractionRate = 0.0;

		/* If there is no intersection, nothing to do */
		if (intersection != null) {

			/* Compute object color */
			pointColor = computeLightning(intersection);

			Primitive primitive = intersection.getObject();

			reflectionRate = primitive.getReflectionRate();
			refractionRate = primitive.getRefractionRate();

			/* Recursive calls to compute reflected and refracted rays colors */
			if (reflectionRate != 0.0)
				reflectColor = traceRay(primitive.getReflected(ray, intersection.getPoint()), nbReflect + 1);
			if (refractionRate != 0.0)
				refractColor = traceRay(primitive.getRefracted(ray, intersection.getPoint()), nbReflect + 1);
		}

		/* Return the combinaison of all colors, weighted by the rates given by the material */
		return combineColors(pointColor, reflectColor, refractColor, reflectionRate, refractionRate);
	}

	/**
	 * Find first intersection from the ray origin.
	 * @param ray Ray.
	 * @return The first intersection or null if there is no intersection.
	 */
	private Intersection getFirstIntersection(Ray ray) {
		Intersection firstIntersection = null;

		/* For each primitive, compute intersection and find the closest */
		for (Primitive primitive : scene.getPrimitives()) {

			Intersection intersection = primitive.intersect(ray);

			/* No intersection */
			if (intersection == null) {
				continue;
			}

			/* Find if this intersection is closer */
			if (firstIntersection == null || intersection.getDistance() < firstIntersection.getDistance()) {
				firstIntersection = intersection;
			}
		}

		return firstIntersection;
	}

	/**
	 * Compute the color of the object intersected.
	 * @param intersection Intersection between the object and the light ray.
	 * @return The color contribution of this light ray.
	 */
	private Color computeLightning(Intersection intersection) {

		/* Init a ray with the intersection point as origin */
		Ray ray = new Ray(intersection.getPoint());

		Double shade = 0.0;

		/* For each lights, find the ones that reach the point (no intersection) */
		for (Light light : scene.getLights()) {

			/* Set the ray direction (from intersection point to the light) */
			ray.setDirection(new Vector3D(ray.getOrigin(), light.getPosition()));

			/* Find if the point is in the shadow of another object ? */
			boolean shadow = false;
			for (Primitive model : scene.getPrimitives()) {
				Intersection objectIntersection = model.intersect(ray);

				/* Intersection found : no contribution from this light */
				if (objectIntersection != null) {
					shadow = true;
					break;
				}
			}

			/* If the ray from the intersection point and the light is not
			 * intersected by another object, the light contributes */
			if (!shadow)
				/* Lambert's cosine law : the cosine between the ray and the
				 * normal to the object surface gives the % of light received */
				shade += intersection.getObject().getCosine(ray);
		}

		/* Negative cosine means the light is behind the object : no lightning */
		if (shade <= 0.0)
			shade = 0.0;

		/* Compute object color */
		Color color = computeBrightness(intersection.getObject().getColor(intersection.getPoint()), shade);

		return color;
	}

	/**
	 * Compute the color seen given object true color and the shade.
	 * @param color Object color.
	 * @param shade Shade %.
	 * @return Color seen.
	 */
	private Color computeBrightness(Color color, Double shade) {
		/* % of the original color kept :
		 * - Ambiant is the minimal ambiant lightning if there are no light source
		 * - (1 - Ambiant) is the contribution of the lights (depends on shade) */
		Double coef = Config.AMBIANT + (1 - Config.AMBIANT) * shade;

		int r = (int) (color.getRed() * coef);
		int g = (int) (color.getGreen() * coef);
		int b = (int) (color.getBlue() * coef);

		r = r > 255 ? 255 : r;
		g = g > 255 ? 255 : g;
		b = b > 255 ? 255 : b;

		return new Color(r, g, b);
	}

	/**
	 * Combines 3 colors given their rates
	 * @param pointColor Object color.
	 * @param reflectColor Reflected ray's color.
	 * @param refractColor Refracted ray's color.
	 * @param reflectionRate Contribution of reflected color.
	 * @param refractionRate Contribution of refracted color.
	 * @return The combined color.
	 */
	private Color combineColors(Color pointColor, Color reflectColor, Color refractColor,
			double reflectionRate, double refractionRate) {
		double diffusionRate = 1 - reflectionRate - refractionRate;
		int r = 0;
		int g = 0;
		int b = 0;

		if (pointColor != null) {
			r += pointColor.getRed() * diffusionRate;
			g += pointColor.getGreen() * diffusionRate;
			b += pointColor.getBlue() * diffusionRate;
		}
		if (reflectColor != null) {
			r += reflectColor.getRed() * reflectionRate;
			g += reflectColor.getGreen() * reflectionRate;
			b += reflectColor.getBlue() * reflectionRate;
		}
		if (refractColor != null) {
			r += refractColor.getRed() * refractionRate;
			g += refractColor.getGreen() * refractionRate;
			b += refractColor.getBlue() * refractionRate;
		}

		return new Color(r, g, b);
	}
}
