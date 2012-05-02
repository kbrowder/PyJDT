package net.kbserve.pyjdt.properties;

import java.io.FileNotFoundException;

import net.kbserve.pyjdt.properties.models_bad.IPersistentProperties;
import net.kbserve.pyjdt.properties.models_bad.PersistentProperties;
import net.kbserve.pyjdt.properties.view.PropertyComposite;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.PropertyPage;

public class PyJDTProps extends PropertyPage {

	PropertyComposite propertyComposite;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public PyJDTProps() {
		super();
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		propertyComposite = new PropertyComposite(parent, SWT.NONE,
				getElement());
		propertyComposite.pack();
		return propertyComposite;
	}

	protected void performDefaults() {
		super.performDefaults();
		PersistentProperties.revert(getElement());
		propertyComposite.pack();
	}

	@Override
	public IProject getElement() {
		return (IProject) super.getElement().getAdapter(IProject.class);
	}

	@Override
	public boolean performOk() {
		// store the value in the owner text field
		
		IPersistentProperties pp = PersistentProperties.load(getElement());

		try {
			pp.save();
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return false;

	}

}