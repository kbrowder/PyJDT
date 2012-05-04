package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class CPESource extends CPEAbstractContainer implements
		ICPEType {

	@Override
	public String getRealPath(IProject project) {
		IClasspathEntry cpe = getClasspath(project);
		IPath value = cpe.getOutputLocation();
		if (value == null) {
			IJavaProject javaProject = null;
			try {
				javaProject = (IJavaProject) project
						.getNature(JavaCore.NATURE_ID);
			} catch (CoreException e) {
			}
			if (javaProject != null) {
				try {
					value = javaProject.getOutputLocation();
				} catch (JavaModelException e) {
				}
			}
		}
		if(value == null) {
			value = cpe.getPath();
		}
		return makeStringPath(prependWorkspaceLoc(value).makeAbsolute());
	}

}
