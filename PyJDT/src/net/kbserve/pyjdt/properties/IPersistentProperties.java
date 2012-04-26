package net.kbserve.pyjdt.properties;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;

public interface IPersistentProperties{
	public Collection<IClasspathInfo> getClasspathInfo();
	public void setClasspathInfo(Collection<IClasspathInfo> classpath);
	public void setClasspathInfo(Collection<IClasspathInfo> classpath, boolean clear);
	public void addClasspathInfo(IClasspathInfo classpath);
	public IClasspathInfo getOrCreateClasspath(IClasspathEntry path);
	public IClasspathInfo getClasspath(String path);
	public void clearClasspaths();
	public boolean isSynchronized();
	public void setSynchronized(boolean sync);
	public void save() throws CoreException, FileNotFoundException;
}
