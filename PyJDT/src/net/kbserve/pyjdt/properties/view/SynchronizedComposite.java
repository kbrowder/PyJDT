package net.kbserve.pyjdt.properties.view;

import net.kbserve.pyjdt.properties.IPersistentProperties;
import net.kbserve.pyjdt.properties.PersistentProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SynchronizedComposite extends Composite {
	private static final String SYNCRONIZE = "Add JDT libraries to PyDev";
	private Button syncCheckbox;
	private IProject project;
	public SynchronizedComposite(Composite parent, int style, IProject project) {
		super(parent, style);
		syncCheckbox = new Button(this, SWT.CHECK);
		syncCheckbox.setText(SYNCRONIZE);
		this.project = project;
	}
	public void setSelection(boolean selected) {
		syncCheckbox.setSelection(selected);
	}
	public void addSelectionListener(SelectionListener listener) {
		syncCheckbox.addSelectionListener(listener);
	}
	public boolean getSelection() {
		return syncCheckbox.getSelection();
	}
	public void removeSelectionListener(SelectionListener listener) {
		syncCheckbox.removeSelectionListener(listener);
	}
	@Override
	public void pack(boolean changed) {
		IPersistentProperties persistentProperties = PersistentProperties
				.load(project);
		this.setSelection(persistentProperties.isSynchronized());
		syncCheckbox.pack(changed);
		super.pack(changed);
	}
	
	

}
