package net.kbserve.pyjdt.properties.models;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.kbserve.pyjdt.Activator;
import net.kbserve.pyjdt.PyDevPaths;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.python.pydev.plugin.nature.PythonNature;

public class PersistentProperties extends ClasspathContainer implements
		IPersistentProperties {

	final protected static Map<IProject, PersistentProperties> props = new HashMap<IProject, PersistentProperties>();

	protected static IPath getLibrariesXml(IProject project) {
		return getWorkingLocation(project).append("libraries.xml");
	};

	protected static IPath getWorkingLocation(IProject project) {
		return project.getWorkingLocation(Activator.PLUGIN_ID);
	}

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

	public static synchronized IClasspathContainer reload(IProject project) {
		props.remove(project);
		return load(project);
	}

	public PersistentProperties() {
		setEnabled(false);
	}

	private IPath getLibrariesXml() {
		return getLibrariesXml(project);
	}

	@Override
	public synchronized boolean isEnabled() {
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
	public synchronized void setEnabled(boolean sync) {
		this.pyjdtSynchronized = sync;

	}

	public static void updateClasspaths(IProject project) {
		IJavaProject jp = JavaCore.create(project);
		IPersistentProperties persistentProperties = load(jp.getProject());
		try {
			if (jp != null) {
				for (IClasspathEntry cp : jp.getRawClasspath()) {
					persistentProperties.getOrCreateChildren(cp);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		for (IClasspathInfo cpi : persistentProperties.getChildren()) {
			IClasspathEntry cpe = cpi.getClasspath(project);
			if (cpi.isEnabled() && cpe != null && persistentProperties.isEnabled()) {
				PyDevPaths.addClasspath(project, cpi);
			} else {
				PyDevPaths.removeClasspath(project, cpi);
			}
		}

	}

	public void addClasspath(IProject project) {

	}

}
