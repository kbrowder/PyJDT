package net.kbserve.pyjdt.properties.models;

import java.util.Collection;

import org.eclipse.jdt.core.IClasspathEntry;

public interface IClasspathContainer {

	public abstract Collection<IClasspathInfo> getChildren();

	public abstract void setChildren(Collection<IClasspathInfo> classpath);

	public abstract void setChildren(Collection<IClasspathInfo> classpath,
			boolean clear);

	public abstract IPropertyAcceptor getOrCreateChildren(IClasspathEntry path);

	public abstract IPropertyAcceptor getChildren(String path);

	public abstract void clearChildren();

}