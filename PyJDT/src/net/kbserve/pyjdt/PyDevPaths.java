package net.kbserve.pyjdt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.kbserve.pyjdt.properties.models.IClasspathInfo;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.python.pydev.core.IPythonPathNature;
import org.python.pydev.core.MisconfigurationException;
import org.python.pydev.core.PythonNatureWithoutProjectException;
import org.python.pydev.plugin.nature.PythonNature;

public class PyDevPaths {

	public static synchronized void addClasspath(IProject project,
			IClasspathInfo classpathInfo) {
		try {
			IClasspathEntry cpe = JavaCore
					.getResolvedClasspathEntry(classpathInfo
							.getClasspath(project));
			IPath value = null;
			switch (cpe.getEntryKind()) {
			case IClasspathEntry.CPE_PROJECT:
				IProject libProj = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(cpe.getPath().lastSegment());
				IJavaProject libJavaProject = (IJavaProject) libProj
						.getNature(JavaCore.NATURE_ID);
				if (libJavaProject != null) {
					value = libJavaProject.getOutputLocation();
				} else {
					value = cpe.getPath();
				}
				break;
			case IClasspathEntry.CPE_SOURCE:
				IPath outputLocation = cpe.getOutputLocation();
				if (outputLocation == null) {
					IJavaProject javaProject = JavaCore.create(project);
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
				return;
			}

			addRelPathToPyDev(project, classpathInfo, value);

		} catch (CoreException e) {
			e.printStackTrace();
		} catch (MisconfigurationException e) {
			e.printStackTrace();
		} catch (PythonNatureWithoutProjectException e) {
			e.printStackTrace();
		}

		// pyNature.getPythonPathNature().setProjectExternalSourcePath(arg0)

	}

	private static void addRelPathToPyDev(IProject project,
			IClasspathInfo classpathInfo, IPath value) throws CoreException,
			MisconfigurationException, PythonNatureWithoutProjectException {
		PythonNature pyNature = getPythonNature(project);
		IPythonPathNature pythonPathNature = pyNature.getPythonPathNature();
		Map<String, String> vars = pythonPathNature.getVariableSubstitution();
		if (value != null) {
			IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot()
					.getLocation().makeAbsolute();
			IPath projectLocation = workspaceLocation.append(
					project.getFullPath()).makeAbsolute();
			value = makeVarPath(value, project);
			if (classpathInfo.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				Set<String> pythonSourcePaths = pythonPathNature
						.getProjectSourcePathSet(false);
				String sourcePath = makeVarPath(
						classpathInfo.getClasspath(project).getPath(), project)
						.toOSString();
				pythonSourcePaths.add(sourcePath);
				pythonPathNature.setProjectSourcePath(makePyDevPath(pythonSourcePaths));
				
			}
			vars.put(classpathInfo.getPath(), value.toOSString());

			vars.put("workspace_loc", workspaceLocation.toOSString());

			vars.put("project_loc", projectLocation.toOSString());

			pythonPathNature.setVariableSubstitution(vars);

			List<String> pathList = pythonPathNature
					.getProjectExternalSourcePathAsList(false);
			String classpathVariableString = variableToExpand(classpathInfo);
			if (!pathList.contains(classpathVariableString)) {
				pathList.add(classpathVariableString);
			}
			pythonPathNature
					.setProjectExternalSourcePath(makePyDevPath(pathList));
		} else {
			System.err.println("value is null");
		}
		pyNature.rebuildPath();
		System.out.println("PyDev path (expanded): "
				+ pythonPathNature.getProjectExternalSourcePathAsList(true));
	}

	private static IPath makeVarPath(IPath value, IProject project) {
		IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot()
				.getLocation().makeAbsolute();
		IPath projectLocation = workspaceLocation.append(project.getFullPath())
				.makeAbsolute();
		value = workspaceLocation.append(value).makeAbsolute();
		if (projectLocation.isPrefixOf(value)) {
			value = value.makeRelativeTo(projectLocation);
			value = new Path(variableToExpand("project_loc")).append(value);
		} else if (workspaceLocation.isPrefixOf(value)) {
			value = value.makeRelativeTo(workspaceLocation);
			value = new Path(variableToExpand("workspace_loc")).append(value);
		} else {
			// nothing
		}
		return value;
	}

	private static String makePyDevPath(Iterable<String> pathList) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (String s : pathList) {
			if (!first) {
				sb.append('|');
			} else {
				first = false;
			}
			sb.append(s);
		}
		return sb.toString();
	}

	public static PythonNature getPythonNature(IProject project) {
		try {
			return (PythonNature) project
					.getNature(PythonNature.PYTHON_NATURE_ID);
		} catch (CoreException e) {
			return null;
		}
	}

	public static synchronized void removeClasspath(IProject project,
			IClasspathInfo classpathInfo) {
		System.out.println("Remove: " + classpathInfo.getPath());
		PythonNature pyNature = getPythonNature(project);
		IPythonPathNature pythonPathNature = pyNature.getPythonPathNature();
		String classpathVariableString = variableToExpand(classpathInfo);
		List<String> pathList;
		try {
			pathList = pythonPathNature
					.getProjectExternalSourcePathAsList(false);
			pathList.remove(classpathVariableString);

			pyNature.getPythonPathNature().setProjectExternalSourcePath(
					makePyDevPath(pathList));
			if (classpathInfo.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				Set<String> pythonSourcePaths = pythonPathNature
						.getProjectSourcePathSet(false);
				String sourcePath = makeVarPath(
						classpathInfo.getClasspath(project).getPath(), project)
						.toOSString();
				pythonSourcePaths.remove(sourcePath);
			}
			pyNature.rebuildPath();
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	private static String variableToExpand(IClasspathInfo classpathInfo) {
		String s = classpathInfo.getPath();
		return variableToExpand(s);
	}

	private static String variableToExpand(String s) {
		return "${" + s + "}";
	}
}
