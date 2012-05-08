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
package net.kbserve.pyjdt.properties;

import net.kbserve.pyjdt.properties.models.RootContainer;
import net.kbserve.pyjdt.properties.view.PropertyComposite;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

// TODO: Auto-generated Javadoc
/**
 * The Class PyJDTProps.
 */
public class PyJDTProps extends PropertyPage {

	/** The property composite. */
	PropertyComposite propertyComposite;

	/**
	 * Instantiates a new py jdt props.
	 */
	public PyJDTProps() {
		super();
	}

	/**
	 * Creates the contents.
	 *
	 * @param parent the parent
	 * @return the control
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		propertyComposite = new PropertyComposite(parent, SWT.NONE,
				getElement());
		propertyComposite.pack();
		return propertyComposite;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		super.performDefaults();
		RootContainer.revert(getElement());
		propertyComposite.pack(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.PropertyPage#getElement()
	 */
	@Override
	public IProject getElement() {
		return (IProject) super.getElement().getAdapter(IProject.class);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		try {
			RootContainer.getRoot(getElement()).save();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

}