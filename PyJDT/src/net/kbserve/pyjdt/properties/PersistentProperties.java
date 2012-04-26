package net.kbserve.pyjdt.properties;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.kbserve.pyjdt.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;

public class PersistentProperties implements IPersistentProperties {
	
	protected static Map<IProject, PersistentProperties> props;
	static {
		props = new HashMap<IProject, PersistentProperties>();
	}
	protected static IPath getLibrariesXml(IProject project) {
		return getWorkingLocation(project).append("libraries.xml");
	};
	protected static IPath getWorkingLocation(IProject project) {
		return project.getWorkingLocation(Activator.PLUGIN_ID);
	}
	private final Map<String, IClasspathInfo> classpaths = new HashMap<String, IClasspathInfo>();//TODO: order..

	private boolean pyjdtSynchronized;

	transient protected IProject project = null;

	public static synchronized IPersistentProperties load(IProject project) {
		PersistentProperties pp = props.get(project);
		if (pp == null) {
			File libxml = getLibrariesXml(project).toFile();
			if (libxml.exists()) {
				try {
					XMLDecoder d = new XMLDecoder(new BufferedInputStream(
							new FileInputStream(libxml)));
					pp = (PersistentProperties) d.readObject();
				} catch (Exception e) {
				}
			}
			if (pp == null) {
				pp = new PersistentProperties();
			}
			props.put(project, pp);
			pp.project = project;
		}
		return pp;
	}
	public static synchronized IPersistentProperties reload(IProject project) {
		props.remove(project);
		return load(project);
	}

	public PersistentProperties() {
		setSynchronized(false);
	}

	@Override
	public void addClasspathInfo(IClasspathInfo classpath) {
		classpaths.put(classpath.getPath(), classpath);

	}

	@Override
	public synchronized void clearClasspaths() {
		classpaths.clear();

	}

	
	@Override
	public synchronized IClasspathInfo getOrCreateClasspath(IClasspathEntry path) {
		String strPath = path.getPath().toPortableString();
		if (!classpaths.containsKey(strPath)) {
			ClasspathInfo value = new ClasspathInfo();
			value.setPath(strPath);
			classpaths.put(strPath, value);
		}
		return classpaths.get(strPath);
	}
	public synchronized IClasspathInfo getClasspath(String path) {
		return classpaths.get(path);
	}

	@Override
	public synchronized Collection<IClasspathInfo> getClasspathInfo() {
		return new LinkedList<IClasspathInfo>(classpaths.values());
	}

	private IPath getLibrariesXml() {
		return getLibrariesXml(project);
	}

	@Override
	public synchronized boolean isSynchronized() {
		return this.pyjdtSynchronized;
	}

	@Override
	public synchronized void save() throws CoreException, FileNotFoundException {
		IPath loc = getLibrariesXml();
		System.out.println("xml:" + loc);
		loc.toFile().getParentFile().mkdirs();
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
				new FileOutputStream(loc.toFile())));
		e.writeObject(this);
		e.close();
	}

	@Override
	public void setClasspathInfo(Collection<IClasspathInfo> classpath) {
		setClasspathInfo(classpath, true);
	}

	@Override
	public synchronized void setClasspathInfo(
			Collection<IClasspathInfo> classpath, boolean clear) {
		if (clear) {
			clearClasspaths();
		}
		for (IClasspathInfo cp : classpath) {
			this.classpaths.put(cp.getPath(), cp);
		}

	}

	@Override
	public synchronized void setSynchronized(boolean sync) {
		this.pyjdtSynchronized = sync;

	}

}
