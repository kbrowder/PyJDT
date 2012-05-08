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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

// TODO: Auto-generated Javadoc
/**
 * The Class CPEAbstractContainer.
 */
public abstract class CPEAbstractContainer implements ICPEType {
	
	/**
	 * Make string path.
	 *
	 * @param classpathEntry the classpath entry
	 * @return the string
	 */
	public static String makeStringPath(IClasspathEntry classpathEntry) {
		return makeStringPath(classpathEntry.getPath());
	}

	/**
	 * Make string path.
	 *
	 * @param path the path
	 * @return the string
	 */
	public static String makeStringPath(IPath path) {
		return path.toPortableString();
	}

	/**
	 * Prepend workspace loc.
	 *
	 * @param value the value
	 * @return the i path
	 */
	protected static IPath prependWorkspaceLoc(IPath value) {
		IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().makeAbsolute();
		value = workspaceLocation.append(value).makeAbsolute();
		return value;
	}

	/** The enabled. */
	private boolean enabled = true;
	
	/** The children. */
	private List<ICPEType> children = new ArrayList<ICPEType>();

	/** The path. */
	private String path = null;
	
	/** The parent path. */
	private String parentPath = null;
	
	/** The available. */
	private boolean available = true;

	/**
	 * Gets the child.
	 *
	 * @param path the path
	 * @return the child
	 */
	public ICPEType getChild(String path) {
		for (ICPEType child : children) {
			if (child != null && child.getPath().equals(path)) {
				return child;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#getChildren()
	 */
	public Collection<ICPEType> getChildren() {
		return new ArrayList<ICPEType>(children);
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#getClasspath(org.eclipse.core.resources.IProject)
	 */
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

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#getParent()
	 */
	@Override
	public String getParent() {
		return this.parentPath;
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#getPath()
	 */
	public String getPath() {
		return path;
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#getRealPath(org.eclipse.core.resources.IProject)
	 */
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

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#hasChild(java.lang.String)
	 */
	public boolean hasChild(String path) {
		for (ICPEType child : children) {
			if (child.getPath().equals(path)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		return this.available;
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#isEnabled()
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Removes the child.
	 *
	 * @param child the child
	 * @return true, if successful
	 */
	protected boolean removeChild(ICPEType child) {
		return children.remove(child);
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#setAvailable(boolean)
	 */
	@Override
	public void setAvailable(boolean available) {
		this.available = available;
		if (!available) {
			for (ICPEType child : getChildren()) {
				child.setAvailable(available);
			}
		}

	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#setChildren(java.util.Collection)
	 */
	@Override
	public void setChildren(Collection<ICPEType> children) {
		this.children.clear();
		this.children.addAll(children);

	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#setParent(java.lang.String)
	 */
	@Override
	public void setParent(String parentPath) {
		this.parentPath = parentPath;
	}

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#setPath(java.lang.String)
	 */
	public void setPath(String path) {
		this.path = path;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPath();
	}

	/**
	 * Class that allows implementation of how classes get updated *.
	 *
	 * @param classpathEntry the classpath entry
	 * @param project the project
	 */
	@Override
	public void update(IClasspathEntry classpathEntry, IProject project) {
	}

	/**
	 * Update child.
	 *
	 * @param child the child
	 * @param project the project
	 * @return the iCPE type
	 */
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
