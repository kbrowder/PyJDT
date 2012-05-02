package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


public class CPEContainerContainer extends AbstractContainer implements IJDTClasspathContainer {

	@Override
	public IJDTClasspathContainer updateChild(IClasspathEntry child, IProject project) {
		IJDTClasspathContainer icp = super.updateChild(child, project);
		IJavaProject javaProject = JavaCore.create(project);
		try {
			IClasspathContainer classpathContainer = JavaCore.getClasspathContainer(child.getPath(), javaProject);
			for(IClasspathEntry containerChild:classpathContainer.getClasspathEntries()) {
				icp.updateChild(containerChild, project);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return icp;
	}

	@Override
	public String getRealPath(IProject project) {
		return "";
	}
	

}
