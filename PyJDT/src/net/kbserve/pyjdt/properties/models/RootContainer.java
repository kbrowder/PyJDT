/********************************************************************************
 *  
 * 
 *  @author Kevin Browder
 * 
 *  Copyright (c) 2012, Kevin Browder All rights reseved
 * 
 *  This file is part of PyJDT
 *  
 *  PyJDT is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
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
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;

public class RootContainer extends CPEAbstractContainer {
	private static final Map<IProject, RootContainer> roots = new HashMap<IProject, RootContainer>();
	private static final Map<RootContainer, IProject> reverseRoots = new HashMap<RootContainer, IProject>();

	protected static IPath getLibrariesXml(IProject project) {
		return prependWorkspaceLoc(getWorkingLocation(project).getFile(
				Activator.PLUGIN_ID + ".root.prefs").getFullPath());
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
			File libxml = getLibrariesXml(project).toFile();
			if (libxml.exists()) {
				System.out.println("Loading from: " + libxml.getAbsolutePath());
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
					e.printStackTrace();
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
		System.out.println("Reverting PyJDT from " + project);
		roots.put(project, null);
		reverseRoots.remove(roots.remove(project));
		assert !roots.containsKey(project);
		return getRoot(project);
	}

	public synchronized void save() throws CoreException, FileNotFoundException {
		try {
			IPath path = getLibrariesXml(reverseRoots.get(this));
			System.out.println("xml:" + path.toOSString());
			path.toFile().getParentFile().mkdirs();
			if (path.toFile().exists()) {
				path.toFile().delete();
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

	@Override
	public Image getIcon() {
		return null;
	}

}
