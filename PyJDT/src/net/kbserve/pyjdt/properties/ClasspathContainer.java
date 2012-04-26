package net.kbserve.pyjdt.properties;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.core.IClasspathEntry;

public class ClasspathContainer implements IClasspathContainer {
	final Map<IClasspathInfo, IClasspathInfo> classpaths = new TreeMap<IClasspathInfo, IClasspathInfo>();
	public ClasspathContainer() {
		super();
	}

	public synchronized void clearChildren() {
		classpaths.clear();
	}

	public synchronized IClasspathInfo getOrCreateChildren(IClasspathEntry path) {
		ClasspathInfo fakeCPE = new ClasspathInfo(path);
		if (!classpaths.containsKey(fakeCPE)) {
			ClasspathInfo value = new ClasspathInfo(path);
			classpaths.put(value, value);
		}
		return classpaths.get(fakeCPE);
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