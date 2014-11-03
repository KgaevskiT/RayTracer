package utils;

import config.Config;


public class ProgressBar {
	private static int currentProgress = 0;

	public static void print(int state) {
		int progress = (int) (((double) state / Config.HEIGHT) * 100);
		if (progress % 25 == 0 && progress != currentProgress) {
			currentProgress = progress;

			System.out.println(progress + "%");
		}
	}
}
