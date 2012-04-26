package net.kbserve.pyjdt.properties;

import java.io.FileNotFoundException;

import org.eclipse.core.runtime.CoreException;

public interface IPersistentProperties extends IClasspathContainer{
	public boolean isSynchronized();
	public void setSynchronized(boolean sync);
	public void save() throws CoreException, FileNotFoundException;
}
