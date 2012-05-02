package net.kbserve.pyjdt.properties.view;

import net.kbserve.pyjdt.properties.models_bad.IClasspathInfo;
import net.kbserve.pyjdt.properties.models_bad.IPersistentProperties;
import net.kbserve.pyjdt.properties.models_bad.PersistentProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class LibraryTableComposite extends Composite {

	private IProject project;
	private Tree table;

	public LibraryTableComposite(Composite parent, int style, IProject project) {
		super(parent, style);
		GridLayout layout = new GridLayout(1, true);
		setLayout(layout);

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		table = new Tree(this, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL
		        | SWT.H_SCROLL);
		//table.setHeaderVisible(true);
		//TreeColumn tc = new TreeColumn(table, SWT.NONE);
		//tc.setWidth(400);
		//tc.setText("Libraries");
		table.setLayout(new FillLayout());
		this.project = project;
	}

	public TreeItem[] getItems() {
		return table.getItems();
	}

	@Override
	public void pack(boolean changed) {
		IPersistentProperties persistentProperties = PersistentProperties
				.load(project);
		PersistentProperties.updateClasspaths(project);
		table.clearAll(true);
		table.setItemCount(0);
		

		/*table.addTreeListener(new TreeListener() {
			
			@Override
			public void treeExpanded(TreeEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void treeCollapsed(TreeEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		})*/
		table.addListener(SWT.Expand, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				System.out.println("SetData: " + arg0);

			}
		});

		table.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				if (arg0.detail == SWT.CHECK) {
					System.out.println("Selection: " + arg0.item);
					try {
						TreeItem tree = (TreeItem) arg0.item;
						IClasspathInfo cpi = (IClasspathInfo) tree.getData();
						cpi.setEnabled(tree.getChecked());
						tree.setExpanded(false);
					} catch (ClassCastException e) {
					}
				}

			}
		});

		for (IClasspathInfo cp : persistentProperties.getChildren()) {
			setupClasspathInfo(table, cp);			
		}

		table.setEnabled(persistentProperties.isEnabled());
		table.pack(changed);
		super.pack(changed);
	}

	private void setupClasspathInfo(Tree tree, IClasspathInfo cp) {
		TreeItem ti = new TreeItem(tree, SWT.NONE);
		ti.setText(cp.getPath());
		ti.setChecked(cp.isEnabled());
		ti.setData(cp);
	}

}
