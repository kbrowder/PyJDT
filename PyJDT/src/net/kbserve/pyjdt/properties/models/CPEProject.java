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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class CPEProject.
 */
public class CPEProject extends CPEAbstractContainer implements ICPEType {


	/* (non-Javadoc)
	 * @see net.kbserve.pyjdt.properties.models.ICPEType#getIcon()
	 */
	@Override
	public Image getIcon() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(new Path(getPath()).segment(0));
		if(project != null) {
			WorkbenchLabelProvider workbenchLabelProvider = new WorkbenchLabelProvider();
			Image ret = workbenchLabelProvider.getImage(JavaCore.create(project));
			if (ret != null) {
				return ret;
			}
		}
		return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_JAR);
	}
}
