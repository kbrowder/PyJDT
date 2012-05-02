package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;


public class CPELibraryContainer extends AbstractContainer implements IJDTClasspathContainer {

	@Override
	public String getRealPath(IProject project) {
		IClasspathEntry cpe = getClasspath(project);
		return makeStringPath(prependWorkspaceLoc(cpe.getPath()).makeAbsolute());
	}
	

}
