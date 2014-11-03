package postprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class AntiAliasing {
	private BufferedImage image;

	public AntiAliasing(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage gradientBlur() {
		BufferedImage gradient = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		for (int h = 0; h < image.getHeight(); h++) {
			for (int w = 0; w < image.getWidth(); w++) {
				Color color = gradient(w, h);

				if (color.getRed() + color.getGreen() + color.getBlue() > 30)
					gradient.setRGB(w, h, mean(w, h).getRGB());
				else
					gradient.setRGB(w, h, image.getRGB(w, h));
			}
		}
		return gradient;
	}

	private Color mean(int w, int h) {
		int r = 0;
		int g = 0;
		int b = 0;
		int pixels = 0;

		if (h != 0) {
			Color up = new Color(image.getRGB(w, h - 1));
			r += up.getRed();
			g += up.getGreen();
			b += up.getBlue();
			pixels++;
		}
		if (h != image.getHeight() - 1) {
			Color down = new Color(image.getRGB(w, h + 1));
			r += down.getRed();
			g += down.getGreen();
			b += down.getBlue();
			pixels++;
		}
		if (w != 0) {
			Color left = new Color(image.getRGB(w - 1, h));
			r += left.getRed();
			g += left.getGreen();
			b += left.getBlue();
			pixels++;
		}
		if (w != image.getWidth() - 1) {
			Color right = new Color(image.getRGB(w + 1, h));
			r += right.getRed();
			g += right.getGreen();
			b += right.getBlue();
			pixels++;
		}

		r /= pixels;
		g /= pixels;
		b /= pixels;

		return new Color(r, g, b);
	}

	private Color gradient(int w, int h) {
		boolean height = false;
		boolean width = false;

		int r = 0;
		int g = 0;
		int b = 0;

		if (h != 0 && h != image.getHeight() - 1) {
			Color up = new Color(image.getRGB(w, h - 1));
			Color down = new Color(image.getRGB(w, h + 1));

			r += Math.abs(up.getRed() - down.getRed());
			g += Math.abs(up.getGreen() - down.getGreen());
			b += Math.abs(up.getBlue() - down.getBlue());
			height = true;
		}
		if (w != 0 && w != image.getWidth() - 1) {

			Color right = new Color(image.getRGB(w + 1, h));
			Color left = new Color(image.getRGB(w - 1, h));

			r += Math.abs(right.getRed() - left.getRed());
			g += Math.abs(right.getGreen() - left.getGreen());
			b += Math.abs(right.getBlue() - left.getBlue());
			width = true;
		}

		if (height && width) {
			r /= 2;
			g /= 2;
			b /= 2;
		}

		return new Color(r, g, b);
	}
}
