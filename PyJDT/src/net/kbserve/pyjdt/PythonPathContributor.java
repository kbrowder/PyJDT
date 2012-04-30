package net.kbserve.pyjdt;

import net.kbserve.pyjdt.properties.models.IClasspathInfo;
import net.kbserve.pyjdt.properties.models.IPersistentProperties;
import net.kbserve.pyjdt.properties.models.PersistentProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.python.pydev.plugin.nature.IPythonPathContributor;

public class PythonPathContributor implements IPythonPathContributor {

	@Override
	public String getAdditionalPythonPath(IProject project) {
		IJavaProject javaProject = null;
		try {
			javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
		}
		if (javaProject != null) {
			PersistentProperties.updateClasspaths(project);
			IPersistentProperties persistentProperties = PersistentProperties
					.load(project);
			StringBuffer finalPath = new StringBuffer();
			boolean first = false;
			try {
				finalPath.append(prependWorkspaceLoc(
						javaProject.getOutputLocation()).toOSString());
			} catch (JavaModelException e1) {
				first = true;
			}
			if (persistentProperties.isEnabled()) {
				for (IClasspathInfo cpi : persistentProperties.getChildren()) {
					IClasspathEntry cpe = cpi.getClasspath(project);
					if (cpe != null && cpi.isEnabled()) {
						cpe = JavaCore.getResolvedClasspathEntry(cpe);
						try {
							String abspath = getClasspathEntryLocation(cpe,
									javaProject);
							if (!first) {
								finalPath.append('|');
							} else {
								first = false;
							}
							finalPath.append(abspath);
						} catch (CoreException e) {
						}
					}
				}
			}
			return finalPath.toString();
		}
		return null;
	}

	private String getClasspathEntryLocation(IClasspathEntry cpe,
			IJavaProject javaProject) throws CoreException, JavaModelException {

		IPath value = null;
		switch (cpe.getEntryKind()) {
		case IClasspathEntry.CPE_PROJECT: {
			IProject libProj = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(cpe.getPath().lastSegment());

			IJavaProject libJavaProject = (IJavaProject) libProj
					.getNature(JavaCore.NATURE_ID);
			if (libJavaProject != null) {
				value = libJavaProject.getOutputLocation();
			} else {
				value = cpe.getPath();
			}
		}
			break;
		case IClasspathEntry.CPE_SOURCE:
			IPath outputLocation = cpe.getOutputLocation();
			if (outputLocation == null) {
				outputLocation = javaProject.getOutputLocation();
			}
			if (outputLocation != null) {
				value = outputLocation;
			} else {
				value = cpe.getPath();
			}
			break;
		case IClasspathEntry.CPE_LIBRARY:
			value = cpe.getPath();
			break;
		default:
			System.out.println("Unsupported classpath: " + cpe);
			break;
		}
		value = prependWorkspaceLoc(value);
		String abspath = value.makeAbsolute().toFile().toString();
		return abspath;
	}

	private IPath prependWorkspaceLoc(IPath value) {
		IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().makeAbsolute();
		value = workspaceLocation.append(value).makeAbsolute();
		return value;
	}
}
