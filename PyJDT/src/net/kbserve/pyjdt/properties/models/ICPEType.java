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

/**
 * The Interface ICPEType is the interface for all IClasspathEntry containers in net.kbserve.pyjdt.
 */
public interface ICPEType {
	//TODO: rename to IClasspathContainer?
	/**
	 * Is the container enabled?
	 * 
	 * @return true, if is enabled
	 */
	public boolean isEnabled();

	/**
	 * Sets the enabled state of the container
	 * 
	 * @param enabled
	 *            the new enabled state.
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Gets the children of this container.
	 * 
	 * @return the children
	 */
	public Collection<ICPEType> getChildren();

	/**
	 * Sets the children sets the children of this container
	 * 
	 * @param children
	 *            the new children
	 */
	public void setChildren(Collection<ICPEType> children);

	/**
	 * Determines if the container has a child with {@link net.kbserve.pyjdt.properties.models.ICPEType.getPath()}==path
	 * 
	 * @param path
	 *            the path
	 * @return true, if it has a child with that path
	 */
	public boolean hasChild(String path);

	/**
	 * Gets the path of the object, note this is the JDT path which is often not an absolute path.
	 * 
	 * @return the path
	 */
	public String getPath();

	/**
	 * Gets the real path, this is the actual (absolute) path of the object.
	 * 
	 * @param project
	 *            the project
	 * @return the real path
	 */
	public String getRealPath(IProject project);

	/**
	 * Sets the path.
	 * 
	 * @param path
	 *            the new path
	 */
	public void setPath(String path);

	/**
	 * Gets the classpath entry for the container
	 * 
	 * @param project
	 *            the project
	 * @return the classpath
	 */
	public IClasspathEntry getClasspath(IProject project);

	/**
	 * Gets the parent path.
	 * 
	 * @return the parent
	 */
	public String getParent();

	/**
	 * Sets the parent path
	 * 
	 * @param parentPath
	 *            the new parent
	 */
	public void setParent(String parentPath);

	/**
	 * Update the current classpath container with a new classpathEntry
	 * 
	 * @param classpathEntry
	 *            the classpath entry
	 * @param project
	 *            the project
	 */
	public void update(IClasspathEntry classpathEntry, IProject project);

	/**
	 * Checks if is this object is available, eg if it's in the environment
	 * 
	 * @return true, if it is available
	 */
	boolean isAvailable();

	/**
	 * Sets the availablity of this object.
	 * 
	 * @param available
	 *            the new available
	 */
	void setAvailable(boolean available);

	/**
	 * Gets the icon, this is what get's displayed in UI's
	 * 
	 * @return the icon
	 */
	public Image getIcon();
}
