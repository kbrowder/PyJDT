package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class CPEContainerContainer extends AbstractContainer implements
		IJDTClasspathContainer {

	private String description = null;

	@Override
	public void update(IClasspathEntry classpathEntry, IProject project) {
		super.update(classpathEntry, project);
		IJavaProject javaProject = JavaCore.create(project);
		System.out.println("ClasspathContainer" + this);
		try {
			IClasspathContainer classpathContainer = JavaCore
					.getClasspathContainer(classpathEntry.getPath(),
							javaProject);
			setDescription(classpathContainer.getDescription());
			for (IClasspathEntry containerChild : classpathContainer
					.getClasspathEntries()) {
				System.out.println("ClasspathContainer: " + containerChild);
				this.updateChild(containerChild, project).update(containerChild, project);
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
