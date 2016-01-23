/**
 * 
 */
package bangersquad.projectile.util;

import java.util.Random;

/**
 * @author feilan
 *
 */
public class RandomNumberUtil {
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	static public int randInt(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}

	/**
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	static public double randDouble(double min, double max) {
		Random r = new Random();
		return r.nextDouble() * (max - min + 1) + min;
	}
	
}
