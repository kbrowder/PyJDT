package net.kbserve.pyjdt.properties.models;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;

public interface IJDTClasspathContainer {
	public Boolean getEnabled();
	public void setEnabled(Boolean enabled);
	public Collection<IJDTClasspathContainer> getChildren();
	public void setChildren(Collection<IJDTClasspathContainer> children);
	public boolean hasChild(String path);
	public IJDTClasspathContainer updateChild(IClasspathEntry child, IProject project);
	public String getPath();
	public String getRealPath(IProject project);
	public void setPath(String path);
	public IClasspathEntry getClasspath(IProject project);
}
