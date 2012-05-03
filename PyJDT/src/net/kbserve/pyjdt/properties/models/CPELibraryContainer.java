package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClasspathEntry;


public class CPELibraryContainer extends AbstractContainer implements IJDTClasspathContainer {

	@Override
	public String getRealPath(IProject project) {
		//TODO: what we should do is get the parent path
		return getPath();//CPE_LIBRARY is always the abspath
	}
	

}
