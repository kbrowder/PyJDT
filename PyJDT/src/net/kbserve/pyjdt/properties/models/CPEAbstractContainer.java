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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public abstract class CPEAbstractContainer implements ICPEType {
	public static String makeStringPath(IClasspathEntry classpathEntry) {
		return makeStringPath(classpathEntry.getPath());
	}

	public static String makeStringPath(IPath path) {
		return path.toPortableString();
	}

	protected static IPath prependWorkspaceLoc(IPath value) {
		IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().makeAbsolute();
		value = workspaceLocation.append(value).makeAbsolute();
		return value;
	}

	private boolean enabled = true;
	private List<ICPEType> children = new ArrayList<ICPEType>();

	private String path = null;
	private String parentPath = null;
	private boolean available = true;

	public ICPEType getChild(String path) {
		for (ICPEType child : children) {
			if (child != null && child.getPath().equals(path)) {
				return child;
			}
		}
		return null;
	}

	public Collection<ICPEType> getChildren() {
		return new ArrayList<ICPEType>(children);
	}

	@Override
	public IClasspathEntry getClasspath(IProject project) {
		IJavaProject javaProject = JavaCore.create(project);
		try {
			for (IClasspathEntry icp : javaProject.getRawClasspath()) {
				if (makeStringPath(icp).equals(getPath())) {
					return icp;
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getParent() {
		return this.parentPath;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String getRealPath(IProject project) {
		if (getPath().length() > 0) {
			Path internalPath = new Path(getPath());
			IResource workspaceResource = ResourcesPlugin.getWorkspace()
					.getRoot().findMember(internalPath);
			if (workspaceResource == null) {
				try {
					workspaceResource = ResourcesPlugin.getWorkspace()
							.getRoot().getFile(internalPath);
				} catch (IllegalArgumentException e) {
					workspaceResource = ResourcesPlugin.getWorkspace()
							.getRoot().getFolder(internalPath);
				}
			}

			IPath rawLocation = workspaceResource.getLocation();
			if (rawLocation != null) {
				return rawLocation.toOSString();
			}

			// not a workspace path
			return internalPath.toOSString();
		}
		return getPath();
	}

	public boolean hasChild(String path) {
		for (ICPEType child : children) {
			if (child.getPath().equals(path)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAvailable() {
		return this.available;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	protected boolean removeChild(ICPEType child) {
		return children.remove(child);
	}

	@Override
	public void setAvailable(boolean available) {
		this.available = available;
		if (!available) {
			for (ICPEType child : getChildren()) {
				child.setAvailable(available);
			}
		}

	}

	@Override
	public void setChildren(Collection<ICPEType> children) {
		this.children.clear();
		this.children.addAll(children);

	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void setParent(String parentPath) {
		this.parentPath = parentPath;
	}

	public void setPath(String path) {
		this.path = path;

	}

	@Override
	public String toString() {
		return getPath();
	}

	/** Class that allows implementation of how classes get updated **/
	@Override
	public void update(IClasspathEntry classpathEntry, IProject project) {
	}

	public synchronized ICPEType updateChild(IClasspathEntry child,
			IProject project) {

		String stringPath = makeStringPath(child);
		ICPEType icp = getChild(stringPath);
		if (icp == null) {
			switch (child.getEntryKind()) {
			case (IClasspathEntry.CPE_CONTAINER):
				icp = new CPEContainer();
				break;
			case (IClasspathEntry.CPE_LIBRARY):
				icp = new CPELibrary();
				break;
			case (IClasspathEntry.CPE_PROJECT):
				icp = new CPEProject();
				break;
			case (IClasspathEntry.CPE_SOURCE):
				icp = new CPESource();
				break;
			case (IClasspathEntry.CPE_VARIABLE):
				icp = new CPEVariable();
				break;
			default:
				throw new UnsupportedOperationException(
						"Unsupported IClasspathEntry.getEntryKind() = '"
								+ child.getEntryKind() + "' on " + child);
			}
			children.add(icp);
			icp.setPath(stringPath);
			icp.setParent(this.getPath());
		}

		return icp;
	}
}
