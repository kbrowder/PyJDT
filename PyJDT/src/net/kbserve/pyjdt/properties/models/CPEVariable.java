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

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.swt.graphics.Image;
// TODO: Auto-generated Javadoc

/**
 * The Class CPEVariable.
 */
public class CPEVariable extends CPEAbstractContainer implements ICPEType {

	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.CPEAbstractContainer#getRealPath(org.eclipse.core.resources.IProject)
	 */
	@Override
	public String getRealPath(IProject project) {
		//TODO: implement me
		throw new UnsupportedOperationException("Variable containers are not yet supported"); 
	}
	
	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#getIcon()
	 */
	@Override
	public Image getIcon() {
		return DebugUITools.getImage(IDebugUIConstants.IMG_OBJS_ENV_VAR);
	}
}
