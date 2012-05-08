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

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class CPEContainer extends CPEAbstractContainer implements
		ICPEType {

	private String description = null;

	@Override
	public void update(IClasspathEntry classpathEntry, IProject project) {
		super.update(classpathEntry, project);
		IJavaProject javaProject = JavaCore.create(project);
		try {
			IClasspathContainer classpathContainer = JavaCore
					.getClasspathContainer(classpathEntry.getPath(),
							javaProject);
			setDescription(classpathContainer.getDescription());
			setAvailable(true);
			for(ICPEType child: getChildren()) {
				child.setAvailable(false);
			}
			for (IClasspathEntry containerChild : classpathContainer
					.getClasspathEntries()) {
				ICPEType child = this.updateChild(containerChild, project);
				child.setAvailable(true);
				child.update(containerChild, project);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getRealPath(IProject project) {
		return "";
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return this.description;
	}
	@Override
	public String toString() {
		if(description != null) {
			return getDescription();
		}
		return super.toString();
	}
	 

}
