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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class LibraryTableComposite extends Composite {

	private IProject project;
	private Table table;
	private TableColumn libraryColumn;
	
	public LibraryTableComposite(Composite parent, int style, IProject project) {
		super(parent, style);
		GridLayout layout = new GridLayout(1, true);
		setLayout(layout);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		table = new Table(this, SWT.BORDER | SWT.CHECK);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		libraryColumn = new TableColumn(table, SWT.CENTER, 0);
		libraryColumn.setText("Library");
		
		this.project = project;
	}
	
	public TableItem[] getItems() {
		return table.getItems();
	}

	@Override
	public void pack(boolean changed) {
		IPersistentProperties persistentProperties = PersistentProperties
				.load(project);
		JDTChangeListener.updateClasspaths(project);
		table.clearAll();
		table.setItemCount(0);
		for (IClasspathInfo cp : persistentProperties.getChildren()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, cp.getPath());
			item.setChecked(cp.isEnabled());
		}
		
		for (TableColumn column : table.getColumns()) {
			column.pack();
		}
		table.setEnabled(persistentProperties.isSynchronized());
		libraryColumn.pack();
		table.pack(changed);
		super.pack(changed);
	}

}
