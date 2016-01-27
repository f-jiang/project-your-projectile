/**
 * 
 */
package bangersquad.projectile.view;

import bangersquad.projectile.ScreenManager;

/**
 * @author feilan
 *
 */
public interface ScreenController {
	
	/**
	 * 
	 * @param manager
	 */
	public void setScreenManager(ScreenManager manager);
	
	public void onScreenSet();
	
}
