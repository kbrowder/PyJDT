/********************************************************************************
 *  
 * 
 *  @author Kevin Browder
 * 
 *  Copyright (c) 2012, Kevin Browder All rights reseved
 * 
 *  This file is part of PyJDT
 *  
 *  PyJDT is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
package net.kbserve.pyjdt.properties.models;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.swt.graphics.Image;

// TODO: Auto-generated Javadoc
/**
 * The Interface ICPEType.
 */
public interface ICPEType {
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled();
	
	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public Collection<ICPEType> getChildren();
	
	/**
	 * Sets the children.
	 *
	 * @param children the new children
	 */
	public void setChildren(Collection<ICPEType> children);
	
	/**
	 * Checks for child.
	 *
	 * @param path the path
	 * @return true, if successful
	 */
	public boolean hasChild(String path);
	
	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath();
	
	/**
	 * Gets the real path.
	 *
	 * @param project the project
	 * @return the real path
	 */
	public String getRealPath(IProject project);
	
	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path);
	
	/**
	 * Gets the classpath.
	 *
	 * @param project the project
	 * @return the classpath
	 */
	public IClasspathEntry getClasspath(IProject project);
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public String getParent();
	
	/**
	 * Sets the parent.
	 *
	 * @param parentPath the new parent
	 */
	public void setParent(String parentPath);
	
	/**
	 * Update.
	 *
	 * @param classpathEntry the classpath entry
	 * @param project the project
	 */
	public void update(IClasspathEntry classpathEntry, IProject project);
	
	/**
	 * Checks if is available.
	 *
	 * @return true, if is available
	 */
	boolean isAvailable();
	
	/**
	 * Sets the available.
	 *
	 * @param available the new available
	 */
	void setAvailable(boolean available);
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public Image getIcon();
}
