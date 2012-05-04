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
