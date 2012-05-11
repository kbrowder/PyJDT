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
package net.kbserve.pyjdt.properties.view;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;


/**
 * The Class PropertyComposite is the root of the property page, it's meant so we can extend and add additional items to the page
 */
public class PropertyComposite extends Composite {

	/** The libs tbl. */
	private LibraryTableComposite libsTbl;

	/**
	 * Instantiates a new property composite.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 * @param project
	 *            the project
	 */
	public PropertyComposite(Composite parent, int style, final IProject project) {
		super(parent, style);
		setLayout(new GridLayout(1, true));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		libsTbl = new LibraryTableComposite(this, SWT.NONE, project);

	}

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public TreeItem[] getItems() {
		return libsTbl.getItems();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#pack(boolean)
	 */
	@Override
	public void pack(boolean changed) {
		libsTbl.pack(changed);
		super.pack(changed);
		getParent().layout(changed, true);

	}

}
