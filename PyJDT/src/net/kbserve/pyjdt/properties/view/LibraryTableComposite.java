package net.kbserve.pyjdt.properties.view;

import net.kbserve.pyjdt.JDTChangeListener;
import net.kbserve.pyjdt.properties.IClasspathInfo;
import net.kbserve.pyjdt.properties.IPersistentProperties;
import net.kbserve.pyjdt.properties.PersistentProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class LibraryTableComposite extends Composite {

	private IProject project;
	private Tree table;

	public LibraryTableComposite(Composite parent, int style, IProject project) {
		super(parent, style);
		GridLayout layout = new GridLayout(1, true);
		setLayout(layout);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		table = new Tree(this, SWT.BORDER | SWT.CHECK);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.project = project;
	}

	public TreeItem[] getItems() {
		return table.getItems();
	}

	@Override
	public void pack(boolean changed) {
		IPersistentProperties persistentProperties = PersistentProperties
				.load(project);
		JDTChangeListener.updateClasspaths(project);
		table.setItemCount(0);
		table.clearAll(true);

		table.addListener(SWT.Expand, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				System.out.println("SetData: " + arg0);

			}
		});

		table.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				if(arg0.detail == SWT.CHECK) {
					System.out.println("Selection: " + arg0.item);
					try {
						TreeItem tree = (TreeItem)arg0.item;
						IClasspathInfo cpi = (IClasspathInfo)tree.getData();
						cpi.setEnabled(tree.getChecked());
					} catch (ClassCastException e)  {}
				}

			}
		});

		for (IClasspathInfo cp : persistentProperties.getChildren()) {
			TreeItem ti = new TreeItem(table, SWT.NONE);
			ti.setText(cp.getPath());
			ti.setChecked(cp.isEnabled());
			ti.setData(cp);
		}

		table.setEnabled(persistentProperties.isEnabled());
		table.pack(changed);
		super.pack(changed);
	}

}
