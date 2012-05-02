package net.kbserve.pyjdt.properties.models;

import org.eclipse.core.resources.IProject;


public class CPEVariableContainer extends AbstractContainer implements IJDTClasspathContainer {

	@Override
	public String getRealPath(IProject project) {
		throw new UnsupportedOperationException("Variable containers are not yet supported"); 
	}

}
