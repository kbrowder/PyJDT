package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ClasspathInfo implements IClasspathInfo {
	//TODO: probably should use JavaCore.getResolvedClasspathEntry to get additional information after the bean has been loaded
	private String path;
	private boolean enabled = true;
	private int entryKind = 0;

	public ClasspathInfo() {
	}

	public ClasspathInfo(IClasspathEntry classpathEntry) {
		setPath(classpathEntry.getPath().toPortableString());
		setEntryKind(classpathEntry.getEntryKind());
		
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	@Override
	public int getEntryKind() {
		return entryKind;
	}

	@Override
	public void setEntryKind(int type) {
		this.entryKind = type;
	}

	@Override
	public IClasspathEntry getClasspath(IProject project) {
		IJavaProject javaProject = JavaCore.create(project);
		try {
			for(IClasspathEntry icp: javaProject.getRawClasspath()) {
				if(icp.getPath().toPortableString().equals(getPath())) {
					return icp;
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void accept(IClasspathVisitor visitor) {
		visitor.visit(this);		
	}
	
	

}
