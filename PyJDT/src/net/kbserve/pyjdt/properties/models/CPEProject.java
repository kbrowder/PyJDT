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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class CPEProject extends CPEAbstractContainer implements
		ICPEType {

	@Override
	public String getRealPath(IProject project) {
		IClasspathEntry cpe = getClasspath(project);
		IProject libProj = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(cpe.getPath().lastSegment());
		IJavaProject libJavaProject = null;
		try {
			libJavaProject = (IJavaProject) libProj
					.getNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
		}
		IPath value = null;
		if (libJavaProject != null) {
			try {
				value = libJavaProject.getOutputLocation();
			} catch (JavaModelException e) {}
		}
		if (value == null) {
			value = cpe.getPath();
		}

		return makeStringPath(prependWorkspaceLoc(value).makeAbsolute());
	}
}
