package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class CPEProjectContainer extends AbstractContainer implements
		IJDTClasspathContainer {

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

		return makeStringPath(value.makeAbsolute());
	}
}
