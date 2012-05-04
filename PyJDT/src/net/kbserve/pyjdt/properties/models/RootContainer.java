package net.kbserve.pyjdt.properties.models;

import java.beans.ExceptionListener;
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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class RootContainer extends CPEAbstractContainer {
	private static final Map<IProject, RootContainer> roots = new HashMap<IProject, RootContainer>();
	private static final Map<RootContainer, IProject> reverseRoots = new HashMap<RootContainer, IProject>();

	protected static IFile getLibrariesXml(IProject project) {
		return getWorkingLocation(project).getFile(
				Activator.PLUGIN_ID + ".root.prefs");
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

	public static synchronized RootContainer getRoot(IProject project) {
		RootContainer rc = roots.get(project);
		if (rc == null) {
			NullProgressMonitor npm = new NullProgressMonitor();
			npm.beginTask("Loading PyJDT data for " + project.getName(), 100);
			File libxml = getLibrariesXml(project).getFullPath().toFile();
			if (libxml.exists()) {
				npm.subTask("Attempting to load: " + libxml.getAbsolutePath());
				npm.internalWorked(5);
				try {
					XMLDecoder d = new XMLDecoder(new BufferedInputStream(
							new FileInputStream(libxml)));
					d.setExceptionListener(new ExceptionListener() {

						@Override
						public void exceptionThrown(Exception e) {
							e.printStackTrace();
						}
					});
					rc = (RootContainer) d.readObject();
					npm.internalWorked(85);
				} catch (Exception e) {
				}
				
				
			}
			if (rc == null) {
				rc = new RootContainer();
				npm.internalWorked(65);
			}
			
			reverseRoots.put(rc, project);
			roots.put(project, rc);
			rc.update();
			npm.done();
		}
		return rc;
	}

	public synchronized void update(IClasspathEntry classpathEntry) {
		this.update();
	}

	public synchronized void update() {
		IProject project = reverseRoots.get(this);
		IJavaProject javaProject = JavaCore.create(project);

		try {
			for (IClasspathEntry icp : javaProject.getRawClasspath()) {
				this.updateChild(icp, project).update(icp, project);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getRealPath(IProject project) {
		// TODO: What about the source path?
		IJavaProject javaProject = JavaCore.create(project);
		try {
			return makeStringPath(prependWorkspaceLoc(
					javaProject.getOutputLocation()).makeAbsolute());
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}

	public static synchronized RootContainer revert(IProject project) {
		reverseRoots.remove(roots.remove(project));
		return getRoot(project);
	}

	public synchronized void save() throws CoreException, FileNotFoundException {
		IFile loc = getLibrariesXml(reverseRoots.get(this));
		System.out.println("xml:" + loc);

		try {
			IPath path = prependWorkspaceLoc(loc.getFullPath());
			path.toFile().getParentFile().mkdirs();
			if (loc.exists()) {
				loc.delete(true, null);
			}
			// loc.create(pis, IResource.HIDDEN, null);//TODO: add progress
			// monitor
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(path.toFile())));
			e.writeObject(this);
			e.close();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}

	}

	@Override
	public String toString() {
		return "Project";
	}
}
