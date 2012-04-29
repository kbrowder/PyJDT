package net.kbserve.pyjdt.properties.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.jdt.core.IClasspathEntry;

public class ClasspathContainer implements IClasspathContainer {
	final Set<IClasspathInfo> classpaths = new HashSet<IClasspathInfo>();
	public ClasspathContainer() {
		super();
	}

	public synchronized void clearChildren() {
		classpaths.clear();
	}

	public synchronized IClasspathInfo getOrCreateChildren(IClasspathEntry path) {
		//this probably should be tied in more with the listener
		for(IClasspathInfo cpi: getChildren()) {
			if(cpi.getPath().equals(path.getPath().toPortableString())) {
				return cpi;
			}
		}
		IClasspathInfo cpi = new ClasspathInfo(path);
		classpaths.add(cpi);
		return cpi;
	}

	public synchronized IClasspathInfo getChildren(String path) {
		for(IClasspathInfo cpi: getChildren()) {
			if(cpi.getPath().equals(path)) {
				return cpi;
			}
		}
		return null;
	}

	public synchronized Collection<IClasspathInfo> getChildren() {
		return new LinkedList<IClasspathInfo>(classpaths);
	}

	public void setChildren(Collection<IClasspathInfo> classpath) {
		setChildren(classpath, true);
	}

	public synchronized void setChildren(Collection<IClasspathInfo> classpath, boolean clear) {
		if (clear) {
			clearChildren();
		}
		for (IClasspathInfo cp : classpath) {
			this.classpaths.add(cp);
		}
	
	}

}