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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class PersistentProperties extends ClasspathContainer implements IPersistentProperties {
	
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

}
