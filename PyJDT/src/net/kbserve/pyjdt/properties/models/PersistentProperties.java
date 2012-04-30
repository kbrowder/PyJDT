package net.kbserve.pyjdt.properties.models;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.kbserve.pyjdt.Activator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class PersistentProperties extends ClasspathContainer implements
		IPersistentProperties {

	final protected static Map<IProject, PersistentProperties> props = new HashMap<IProject, PersistentProperties>();

	protected static IFile getLibrariesXml(IProject project) {
		return getWorkingLocation(project).getFile(
				Activator.PLUGIN_ID + ".prefs");
	};

	protected static IFolder getWorkingLocation(IProject project) {
		IFolder settings = project.getFolder(".settings");
		if (!settings.exists()) {
			try {
				settings.create(IResource.HIDDEN, true, null);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
		IFolder pluginFolder = settings.getFolder(Activator.PLUGIN_ID);
		if (!pluginFolder.exists()) {
			try {
				pluginFolder.create(IResource.HIDDEN, true, null);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}
		return pluginFolder;
	}

	public static synchronized IPersistentProperties load(IProject project) {
		PersistentProperties pp = props.get(project);
		if (pp == null) {
			File libxml = getLibrariesXml(project).getFullPath().toFile();
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

	}

	private boolean pyjdtSynchronized;

	transient protected IProject project = null;

	public PersistentProperties() {
		setEnabled(false);
	}

	@Override
	public void accept(IClasspathVisitor visitor) {
		visitor.visit(this);
	}

	private IFile getLibrariesXml() {
		return getLibrariesXml(project);
	}

	@Override
	public synchronized boolean isEnabled() {
		return this.pyjdtSynchronized;
	}

	@Override
	public synchronized void save() throws CoreException, FileNotFoundException {
		IFile loc = getLibrariesXml();
		System.out.println("xml:" + loc);

		try {

			loc.getFullPath().toFile().getParentFile().mkdirs();
			if (loc.exists()) {
				loc.delete(true, null);
			}
			// loc.create(pis, IResource.HIDDEN, null);//TODO: add progress
			// monitor
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(loc.getFullPath().toFile())));
			e.writeObject(this);
			e.close();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}

	}

	@Override
	public synchronized void setEnabled(boolean sync) {
		this.pyjdtSynchronized = sync;

	}

}
