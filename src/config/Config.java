package config;

public class Config {
	/* Render image size */
	public static int WIDTH = 1000;
	public static int HEIGHT = 1000;

	/* Anti aliasing post process */
	public static boolean ANTI_ALIASING = false;

	/* Ambient lightning (% of lighting for points not in range of a light) */
	public static double AMBIANT = 0.05;

	/* Maximum recursive reflection or refraction */
	public static int MAX_REFLEXION = 3;
	/* Not used yet */
	public static double MAX_DEPTH ;
}