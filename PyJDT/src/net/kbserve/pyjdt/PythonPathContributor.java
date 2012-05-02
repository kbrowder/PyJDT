package net.kbserve.pyjdt;

import net.kbserve.pyjdt.properties.models.IJDTClasspathContainer;
import net.kbserve.pyjdt.properties.models.RootContainer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.python.pydev.plugin.nature.IPythonPathContributor;

public class PythonPathContributor implements IPythonPathContributor {

	protected StringBuffer makePaths(IJDTClasspathContainer classpathContainer,
			IJavaProject javaProject) {
		IProject project = javaProject.getProject();
		StringBuffer sb = new StringBuffer();
		if (classpathContainer.isEnabled()) {
			sb.append(classpathContainer.getRealPath(project));
			boolean first = sb.length()==0;
			for(IJDTClasspathContainer child: classpathContainer.getChildren()) {
				StringBuffer childStringBuffer = makePaths(child, javaProject);
				if (first==false) {
					sb.append("|");
				} else {
					first = false;
				}
				sb.append(childStringBuffer);
			}
			
		}
		return sb;
	}

	@Override
	public String getAdditionalPythonPath(IProject project) {
		IJavaProject javaProject = null;
		try {
			javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
		} catch (CoreException e) {
		}
		if (javaProject != null) {
			RootContainer root = RootContainer.getRoot(project);
			root.update();
			return makePaths(root, javaProject).toString();
		}
		return null;
	}

}
