package net.kbserve.pyjdt.properties.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public abstract class AbstractContainer implements IJDTClasspathContainer {
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

	private Boolean enabled = null;
	private List<IJDTClasspathContainer> children = new ArrayList<IJDTClasspathContainer>();

	private String path = null;

	public IJDTClasspathContainer getChild(String path) {
		for (IJDTClasspathContainer child : children) {
			if (child.getPath().equals(path)) {
				return child;
			}
		}
		return null;
	}

	public Collection<IJDTClasspathContainer> getChildren() {
		return new ArrayList<IJDTClasspathContainer>(children);
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

	public String getPath() {
		return path;
	}

	public boolean hasChild(String path) {
		for (IJDTClasspathContainer child : children) {
			if (child.getPath().equals(path)) {
				return true;
			}
		}
		return false;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	@Override
	public void setChildren(Collection<IJDTClasspathContainer> children) {
		this.children.clear();
		this.children.addAll(children);

	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setPath(String path) {
		this.path = path;

	}

	@Override
	public synchronized IJDTClasspathContainer updateChild(
			IClasspathEntry child, IProject project) {

		String stringPath = makeStringPath(child);
		IJDTClasspathContainer icp = getChild(stringPath);
		if (icp == null) {
			switch (child.getEntryKind()) {
			case (IClasspathEntry.CPE_CONTAINER):
				icp = new CPEContainerContainer();
				break;
			case (IClasspathEntry.CPE_LIBRARY):
				icp = new CPELibraryContainer();
				break;
			case (IClasspathEntry.CPE_PROJECT):
				icp = new CPEProjectContainer();
				break;
			case (IClasspathEntry.CPE_SOURCE):
				icp = new CPESourceContainer();
				break;
			case (IClasspathEntry.CPE_VARIABLE):
				icp = new CPEVariableContainer();
				break;
			default:
				throw new UnsupportedOperationException(
						"Unsupported IClasspathEntry.getEntryKind() = '"
								+ child.getEntryKind() + "' on " + child);
			}
			children.add(icp);
		}
		icp.setPath(stringPath);
		return icp;

	}
}
