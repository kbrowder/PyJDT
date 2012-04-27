package net.kbserve.pyjdt.properties.models;

import java.io.FileNotFoundException;

import org.eclipse.core.runtime.CoreException;

public interface IPersistentProperties extends IClasspathContainer{
	public boolean isEnabled();
	public void setEnabled(boolean sync);
	public void save() throws CoreException, FileNotFoundException;
}
