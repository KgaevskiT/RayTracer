package raytracer;
import geometry.Intersection;
import geometry.Vector3D;
import geometry.primitive.Light;
import geometry.primitive.Primitive;
import geometry.primitive.Scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveTask;

import postprocessing.AntiAliasing;
import config.Config;


public class RayTracer extends RecursiveTask<BufferedImage> {
	private Scene scene;
	private int begin;
	private int height;
	private boolean fork;

	/**
	 * Create a multithreaded RayTracer for a scene.
	 * @param scene
	 */
	public RayTracer(Scene scene) {
		this.scene = scene;
		this.begin = 0;
		this.height = Config.HEIGHT;
		this.fork = true;
	}

	/**
	 * Create a RayTracer that compute a part of the render image.
	 * @param scene
	 * @param begin Height index to start.
	 * @param height Height of the render image.
	 */
	public RayTracer(Scene scene, int begin, int height) {
		this.scene = scene;
		this.begin = begin;
		this.height = height;
		this.fork = false;	// Only fork once
	}

	/**
	 * Main RayTracer function: Compute the render image of the scene.
	 * @return Render image of the scene.
	 */
	public BufferedImage render() {
		BufferedImage image = new BufferedImage(Config.WIDTH, height, BufferedImage.TYPE_INT_RGB);

		/* Compute color for each pixel */
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < Config.WIDTH; w++) {

				/* Get a ray from the camera to the scene */
				Ray ray = scene.getCamera().getRay(h + begin, w);

				/* Calculate the color of the pixel */
				Color color = traceRay(ray, 0);
				image.setRGB(w, h, color.getRGB());
			}
		}

		System.out.println("Thread finished");

		/* Post processing */
		if (Config.ANTI_ALIASING)
			return new AntiAliasing(image).gradientBlur();
		else
			return image;
	}

	/**
	 * Recursively compute the color of a pixel.
	 * @param ray Incoming ray (from camera or from reflection or from refraction)
	 * @param nbReflect Current number of reflections or refractions made.
	 * @return The color of the pixel.
	 */
	public Color traceRay(Ray ray, int nbReflect) {

		/* Stop recursion if maximum number of reflections if reached */
		if (nbReflect >= Config.MAX_REFLEXION)
			return null;

		Color pointColor = null;	// Diffusion light: Color of the object.
		Color reflectColor = null;	// Color from the reflected ray.
		Color refractColor = null;	// Color from the refracted ray.

		Intersection intersection = getFirstIntersection(ray);	// closest intersection

		double reflectionRate = 0;
		double refractionRate = 0;

		/* No intersection: Nothing to do */
		if (intersection != null) {
			pointColor = computeLightning(intersection);	// Compute object color
			Primitive primitive = intersection.getObject();	// Object intersected

			reflectionRate = primitive.getReflectionRate();
			refractionRate = primitive.getRefractionRate();

			/* Recursive calls to compute reflected and refracted rays colors */
			if (reflectionRate != 0)
				reflectColor = traceRay(primitive.getReflected(ray, intersection), nbReflect + 1);
			if (refractionRate != 0)
				refractColor = traceRay(primitive.getRefracted(ray, intersection), nbReflect + 1);
		}

		/* Return the combination of all colors, weighted by the rates given by the material */
		return combineColors(pointColor, reflectColor, refractColor, reflectionRate, refractionRate);
	}

	/**
	 * Calculate the closest intersection from the ray origin.
	 * @param ray Ray.
	 * @return The closest intersection or null if there is no intersection.
	 */
	private Intersection getFirstIntersection(Ray ray) {
		Intersection closest = null;

		/* For each primitive, compute intersection and find the closest */
		for (Primitive primitive : scene.getPrimitives()) {

			Intersection intersection = primitive.intersect(ray);

			/* No intersection */
			if (intersection == null)
				continue;

			/* Calculate if the intersection is closer */
			if (closest == null || intersection.getDistance() < closest.getDistance()) {
				closest = intersection;
			}
		}

		return closest;
	}

	/**
	 * Compute the color of the object intersected.
	 * @param intersection Intersection between the object and the light ray.
	 * @return The color contribution of this light ray.
	 */
	private Color computeLightning(Intersection intersection) {

		/* Init a ray with the intersection point as origin */
		Ray ray = new Ray(intersection.getPoint());

		double transparency = 1;
		double shade = 0;

		/* For each lights, find the ones that reach the point (no intersection) */
		for (Light light : scene.getLights()) {
			transparency = 1;

			/* Set the ray direction (from intersection point to the light) */
			ray.setDirection(new Vector3D(ray.getOrigin(), light.getPosition()));
			double distance = ray.getOrigin().distance(light.getPosition());

			/* Find if the point is in the shadow of another object ? */
			for (Primitive model : scene.getPrimitives()) {
				Intersection objectIntersection = model.intersect(ray);

				/* Intersection found */
				if (objectIntersection != null) {
					/* Intersection between object and light */
					if (Math.sqrt(intersection.getDistance()) < distance) {
						transparency *= objectIntersection.getObject().getRefractionRate();
					}
				}
			}

			/* Lambert's cosine law : the cosine between the ray and the
			 * normal to the object surface gives the % of light received */
			double lightning = transparency * intersection.getObject().getCosine(ray, intersection);

			if (lightning > shade)
				shade = lightning;
		}

		/* Negative cosine means the light is behind the object : no lightning */
		if (shade <= 0)
			shade = 0;

		/* Compute object color */
		Color color = computeBrightness(intersection.getObject().getColor(intersection), shade);

		return color;
	}

	/**
	 * Compute the color seen given object true color and the shade.
	 * @param color Object color.
	 * @param shade Shade %.
	 * @return Color seen.
	 */
	private Color computeBrightness(Color color, double shade) {
		/* % of the original color kept :
		 * - Ambiant is the minimal ambiant lightning if there are no light source
		 * - (1 - Ambiant) is the contribution of the lights (depends on shade) */
		double coef = Config.AMBIANT + (1 - Config.AMBIANT) * shade;

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

	@Override
	protected BufferedImage compute() {
		if (fork) {
			BufferedImage image = new BufferedImage(Config.WIDTH, Config.HEIGHT, BufferedImage.TYPE_INT_RGB);

			int height = Config.HEIGHT / 4;

			RayTracer rt1 = new RayTracer(scene, 0, height);
			RayTracer rt2 = new RayTracer(scene, height, height * 2);
			RayTracer rt3 = new RayTracer(scene, height * 2, height * 3);
			RayTracer rt4 = new RayTracer(scene, height * 3, height * 4);

			rt2.fork();
			rt3.fork();
			rt4.fork();

			BufferedImage i1 = rt1.compute();
			BufferedImage i2 = rt2.join();
			BufferedImage i3 = rt3.join();
			BufferedImage i4 = rt4.join();

			for (int h = 0; h < height; h++)
				for (int w = 0; w < Config.WIDTH; w++)
					image.setRGB(w, h, i1.getRGB(w, h));
			for (int h = height; h < height * 2; h++)
				for (int w = 0; w < Config.WIDTH; w++)
					image.setRGB(w, h, i2.getRGB(w, h - height));
			for (int h = height * 2; h < height * 3; h++)
				for (int w = 0; w < Config.WIDTH; w++)
					image.setRGB(w, h, i3.getRGB(w, h - 2 * height));
			for (int h = height * 3; h < height * 4; h++)
				for (int w = 0; w < Config.WIDTH; w++)
					image.setRGB(w, h, i4.getRGB(w, h - 3 * height));

			return image;
		} else
			return render();
	}
}
