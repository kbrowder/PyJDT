package net.kbserve.pyjdt.properties.view;

import net.kbserve.pyjdt.properties.IPersistentProperties;
import net.kbserve.pyjdt.properties.PersistentProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

public class PropertyComposite extends Composite {
	private SynchronizedComposite syncCheckbox;
	private LibraryTableComposite libsTbl;

	public PropertyComposite(Composite parent, int style, final IProject project) {
		super(parent, style);
		GridLayout layout = new GridLayout(1, true);
		setLayout(layout);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false);
		data.grabExcessHorizontalSpace = true;
		setLayoutData(data);

		syncCheckbox = new SynchronizedComposite(this, SWT.NONE, project);
		syncCheckbox.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				IPersistentProperties persistentProperties = PersistentProperties
						.load(project);
				persistentProperties.setEnabled((syncCheckbox)
						.getSelection());

				pack(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {

			}
		});
		libsTbl = new LibraryTableComposite(this, SWT.NONE, project);

		syncCheckbox.setSelection(PersistentProperties.load(project)
				.isEnabled());
	}

	public boolean getSelection() {
		return syncCheckbox.getSelection();
	}

	public TreeItem[] getItems() {
		return libsTbl.getItems();
	}

	@Override
	public void pack(boolean changed) {
		syncCheckbox.pack(changed);
		libsTbl.pack(changed);
		super.pack(changed);
		getParent().layout(changed, true);
	}

}
