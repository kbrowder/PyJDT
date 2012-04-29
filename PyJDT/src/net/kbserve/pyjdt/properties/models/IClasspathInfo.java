package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;



public interface IClasspathInfo {
	public String getPath();
	public void setPath(String path);
	public boolean isEnabled();
	public void setEnabled(boolean enabled);
	public int getEntryKind();
	public void setEntryKind(int entryKind);
	public IClasspathEntry getClasspath(IProject project);
}
