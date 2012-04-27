package net.kbserve.pyjdt.properties.models;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

public class ClasspathContainer implements IClasspathContainer {
	final Map<IClasspathInfo, IClasspathInfo> classpaths = new TreeMap<IClasspathInfo, IClasspathInfo>();
	public ClasspathContainer() {
		super();
	}

	public synchronized void clearChildren() {
		classpaths.clear();
	}

	public synchronized IClasspathInfo getOrCreateChildren(IClasspathEntry path) {
		//this probably should be tied in more with the listener
		ClasspathInfo fakeCPE = new ClasspathInfo(path);
		if (!classpaths.containsKey(fakeCPE)) {
			ClasspathInfo value = new ClasspathInfo(path);
			classpaths.put(value, value);
		}
		IClasspathInfo returnValue = classpaths.get(fakeCPE);
		return returnValue;
	}

	public synchronized IClasspathInfo getChildren(String path) {
		ClasspathInfo cpi = new ClasspathInfo();
		cpi.setPath(path);
		return classpaths.get(cpi);
	}

	public synchronized Collection<IClasspathInfo> getChildren() {
		return new LinkedList<IClasspathInfo>(classpaths.keySet());
	}

	public void setChildren(Collection<IClasspathInfo> classpath) {
		setChildren(classpath, true);
	}

	public synchronized void setChildren(Collection<IClasspathInfo> classpath, boolean clear) {
		if (clear) {
			clearChildren();
		}
		for (IClasspathInfo cp : classpath) {
			this.classpaths.put(cp, cp);
		}
	
	}

}