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
package net.kbserve.pyjdt;

import net.kbserve.pyjdt.properties.models.ICPEType;
import net.kbserve.pyjdt.properties.models.RootContainer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.python.pydev.plugin.nature.IPythonPathContributor;

// TODO: Auto-generated Javadoc
/**
 * The Class PythonPathContributor.
 */
public class PythonPathContributor implements IPythonPathContributor {

	/**
	 * Make paths.
	 *
	 * @param classpathContainer the classpath container
	 * @param javaProject the java project
	 * @return the string buffer
	 */
	protected StringBuffer makePaths(ICPEType classpathContainer,
			IJavaProject javaProject) {
		IProject project = javaProject.getProject();
		StringBuffer sb = new StringBuffer();
		if (classpathContainer.isEnabled()) {
			sb.append(classpathContainer.getRealPath(project));
			boolean first = sb.length() == 0;
			for (ICPEType child : classpathContainer.getChildren()) {
				StringBuffer childStringBuffer = makePaths(child, javaProject);
				if (first == false) {
					if (childStringBuffer.length() > 0) {
						sb.append("|");
					}
				} else {
					first = false;
				}
				sb.append(childStringBuffer);
			}

		}
		return sb;
	}

	/* (non-Javadoc)
	 * @see org.python.pydev.plugin.nature.IPythonPathContributor#getAdditionalPythonPath(org.eclipse.core.resources.IProject)
	 */
	@Override
	public String getAdditionalPythonPath(IProject project) {
		IJavaProject javaProject = null;
		try {
			javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
		}
		if (javaProject != null) {
			RootContainer root = RootContainer.getRoot(project);
			root.update();
			String paths = makePaths(root, javaProject).toString();
			System.out.println("PyJDT path:" + paths);
			return paths;
		}
		return null;
	}

}
