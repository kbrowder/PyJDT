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

public interface ICPEType {
	public boolean isEnabled();
	public void setEnabled(boolean enabled);
	public Collection<ICPEType> getChildren();
	public void setChildren(Collection<ICPEType> children);
	public boolean hasChild(String path);
	public String getPath();
	public String getRealPath(IProject project);
	public void setPath(String path);
	public IClasspathEntry getClasspath(IProject project);
	public String getParent();
	public void setParent(String parentPath);
	public void update(IClasspathEntry classpathEntry, IProject project);
	boolean isAvailable();
	void setAvailable(boolean available);
}
