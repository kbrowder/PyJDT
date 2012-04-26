/**
 * 
 */
package net.kbserve.pyjdt.startup;

import net.kbserve.pyjdt.JDTChangeListener;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;

/**
 * @author KBrowder
 *
 */
public class Startup implements IStartup {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	@Override
	public void earlyStartup() {
		JavaCore.addElementChangedListener(new JDTChangeListener());

	}

}
