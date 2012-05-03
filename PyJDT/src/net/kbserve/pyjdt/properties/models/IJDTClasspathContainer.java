package net.kbserve.pyjdt.properties.models;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;

public interface IJDTClasspathContainer {
	public boolean isEnabled();
	public void setEnabled(boolean enabled);
	public boolean isNoPreference();
	public void setNoPrefererence(boolean prefered);
	public Collection<IJDTClasspathContainer> getChildren();
	public void setChildren(Collection<IJDTClasspathContainer> children);
	public boolean hasChild(String path);
	public String getPath();
	public String getRealPath(IProject project);
	public void setPath(String path);
	public IClasspathEntry getClasspath(IProject project);
	public String getParent();
	public void setParent(String parentPath);
	public void update(IClasspathEntry classpathEntry, IProject project);
}
