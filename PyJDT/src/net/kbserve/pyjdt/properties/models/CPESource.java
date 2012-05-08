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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class CPESource extends CPEAbstractContainer implements ICPEType {

	@Override
	public Image getIcon() {
		IFile file = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(new Path(getPath()));
		IProject project = file.getProject();
		if (project != null) {
			WorkbenchLabelProvider workbenchLabelProvider = new WorkbenchLabelProvider();
			System.out.println("file.getRawLocation:" + file.getRawLocation());
			Image ret = workbenchLabelProvider.getImage(project.findMember(file
					.getRawLocation()));
			System.out.println("source:" + file.getRawLocation());
			System.out.println("ret:" + ret);
			if (ret != null) {
				return ret;
			}
		}
		return PlatformUI.getWorkbench().getSharedImages()
				.getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
	}

}
