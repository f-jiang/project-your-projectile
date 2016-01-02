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
	
	static public int getRandomInt(int min, int max) {
		Random r = new Random();	// TODO: why create a new one every time?
		return r.nextInt(max - min + 1) + min;
	}
}
