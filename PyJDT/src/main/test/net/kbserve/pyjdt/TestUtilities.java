package net.kbserve.pyjdt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Assert;
import org.python.pydev.core.IPythonPathNature;
import org.python.pydev.plugin.nature.PythonNature;

public class TestUtilities {

	protected static final Map<String, IProject> projects = new HashMap<String, IProject>();

	public static IJavaProject createJDTProject(String projectName)
			throws CoreException {
		return createJDTProject(projectName, "src");
	}

	public static IJavaProject createJDTProject(String projectName,
			String sourceDir) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName);

		project.create(null);
		project.open(null);

		IProjectDescription description = project.getDescription();
		description.setComment("Project made for unittests");
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);

		Assert.assertTrue("Project doesn't exist: " + projectName,
				project.exists());
		Assert.assertTrue("Project is not open", project.isOpen());
		System.out.println(project.getLocation());

		IJavaProject jdtProject = JavaCore.create(project);

		IFolder binFolder = project.getFolder("bin");
		if (!binFolder.exists()) {
			binFolder.create(false, true, null);
		}

		System.out.println("Created " + binFolder.getFullPath());
		if (sourceDir != null) {
			addSourceFolder(project, sourceDir, true);
		}
		jdtProject.setOutputLocation(binFolder.getFullPath(), null);
		jdtProject.setRawClasspath(new IClasspathEntry[] {}, null);

		projects.put(projectName, project);

		System.out.println("Created project: " + projectName + " ("
				+ project.getLocation() + ")");
		return jdtProject;
	}

	public static void addLibraryContainer(IPath container, IJavaProject project)
			throws JavaModelException {
		IClasspathEntry containerEntry = JavaCore.newContainerEntry(container);
		addToClasspath(project, containerEntry);
	}

	public static void addPyDevNature(IProject project, String sourceDir)
			throws CoreException {
		if (sourceDir != null) {
			PythonNature.addNature(project, null,
					PythonNature.JYTHON_VERSION_2_5,
					project.findMember(sourceDir).getLocation()
							.toPortableString(), null, null, null);
		} else {
			PythonNature.addNature(project, null,
					PythonNature.JYTHON_VERSION_2_5,
					project.findMember(sourceDir).getLocation()
							.toPortableString(), null, null, null);
		}
	}

	public static void addSourceFolder(IProject project, String sourceDir,
			boolean removeOtherSourceFolders) throws CoreException {
		IFolder src = project.getFolder(sourceDir);
		if (!src.exists()) {
			src.create(false, true, null);
		}
		if (project.hasNature(JavaCore.NATURE_ID)) {
			IJavaProject javaProject = JavaCore.create(project);
			IPackageFragmentRoot sourceFragment = javaProject
					.getPackageFragmentRoot(src);
			IClasspathEntry newSourceEntry = JavaCore
					.newSourceEntry(sourceFragment.getPath());
			if (removeOtherSourceFolders) {
				LinkedList<IClasspathEntry> filteredClasspaths = new LinkedList<IClasspathEntry>();
				int removeKind = IClasspathEntry.CPE_SOURCE;
				for (IClasspathEntry cpe : javaProject.getRawClasspath()) {
					if (cpe.getEntryKind() != removeKind) {
						filteredClasspaths.add(cpe);
					}
				}
				javaProject.setRawClasspath(
						filteredClasspaths.toArray(new IClasspathEntry[0]),
						null);
			}
			addToClasspath(javaProject, newSourceEntry);
		}
		if (project.hasNature(PythonNature.PYTHON_NATURE_ID)) {
			IPythonPathNature pythonPathNature = PythonNature.getPythonNature(
					project).getPythonPathNature();
			String pyPath = pythonPathNature.getProjectSourcePath(false);
			String localString = src.getLocation().toPortableString();
			if (pyPath.toString().matches(".*\\b" + localString + "\\b.*")) {
				if (pyPath.length() > 0) {
					pyPath += '|';
				}
				pyPath += localString;
				System.out.println("New PyDev path for project "
						+ project.getName() + " is '" + pyPath + "'");
				pythonPathNature.setProjectSourcePath(pyPath);
			} else {
				System.out.println("The PyDev path already contains "
						+ localString + " for project " + project.getName());
			}
		}

	}

	protected static void addToClasspath(IJavaProject project,
			IClasspathEntry newEntry) throws JavaModelException {
		IClasspathEntry[] origionalEntries = project.getRawClasspath();

		boolean duplicated = false;
		for (IClasspathEntry ce : origionalEntries) {
			System.out.println("ice:" + ce.getPath());
			if (ce.getPath().equals(newEntry)) {
				duplicated = true;
				break;
			}
		}
		if (!duplicated) {
			IClasspathEntry[] newEntries = Arrays.copyOf(origionalEntries,
					origionalEntries.length + 1);
			newEntries[origionalEntries.length] = newEntry;
			project.setRawClasspath(newEntries, null);
		}
		for (IClasspathEntry ice : project.getRawClasspath()) {
			if (ice.equals(newEntry)) {
				return;
			}
		}
		throw new AssertionFailedError("The IClasspathEntry " + newEntry
				+ " was not added to the project "
				+ project.getProject().getName());
	}

	public static void deleteAllTestProjects() throws CoreException {
		for (IProject project : projects.values()) {
			project.delete(true, null);
			System.out.println("Deleted project named: " + project.getName());
		}
	}
	public static IProject getProject(String name) {
		return projects.get(name);
	}

}