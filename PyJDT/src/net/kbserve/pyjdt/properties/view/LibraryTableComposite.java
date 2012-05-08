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

import net.kbserve.pyjdt.properties.models.ICPEType;
import net.kbserve.pyjdt.properties.models.RootContainer;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

// TODO: Auto-generated Javadoc
/**
 * The Class LibraryTableComposite.
 */
public class LibraryTableComposite extends Composite {

	/**
	 * The listener interface for receiving tableSelection events.
	 * The class that is interested in processing a tableSelection
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addTableSelectionListener<code> method. When
	 * the tableSelection event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see TableSelectionEvent
	 */
	private final class TableSelectionListener implements SelectionListener {
		
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			if ((arg0.detail & SWT.CHECK) == SWT.CHECK) {
				try {

					TreeItem tree = (TreeItem) arg0.item;
					ICPEType cpc = (ICPEType) tree.getData();
					cpc.setEnabled(tree.getChecked());
					System.out.println("Selection: " + arg0.item + " -> "
							+ cpc.isEnabled());
					TreeItem parentItem = tree.getParentItem();
					if (!tree.getChecked()) {
						if (parentItem.getChecked()) {
							parentItem.setGrayed(true);
						} else {

						}
					} else {
						tree.setGrayed(false);
					}
					updateAll(tree, tree.getChecked());
					makeChecksConsistentWithChildren();
				} catch (ClassCastException e) {
					e.printStackTrace();
				}
			}

		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
		}
	}

	/**
	 * Update all.
	 *
	 * @param parent the parent
	 * @param checked the checked
	 */
	protected void updateAll(TreeItem parent, boolean checked) {
		for (TreeItem child : parent.getItems()) {
			child.setChecked(checked);
			child.setGrayed(false);
			updateAll(child, checked);
		}
	}

	/**
	 * Make checks consistent with children.
	 *
	 * @param item the item
	 * @return null if checkbox should be gray, true if checked, false if
	 * unchecked
	 */
	private Boolean makeChecksConsistentWithChildren(TreeItem item) {
		Boolean ret = item.getChecked();
		for (TreeItem child : item.getItems()) {
			Boolean current = makeChecksConsistentWithChildren(child);
			if (current != ret) {
				ret = null;
			}
		}
		item.setChecked(!Boolean.FALSE.equals(ret));
		item.setGrayed(ret == null);
		return ret;
	}

	/**
	 * Make checks consistent with children.
	 */
	private void makeChecksConsistentWithChildren() {
		for (TreeItem item : table.getItems()) {
			makeChecksConsistentWithChildren(item);
		}
	}

	/** The project. */
	private IProject project;
	
	/** The table. */
	private Tree table;
	
	/** The table selection listener. */
	private TableSelectionListener tableSelectionListener;

	/**
	 * Instantiates a new library table composite.
	 *
	 * @param parent the parent
	 * @param style the style
	 * @param project the project
	 */
	public LibraryTableComposite(Composite parent, int style, IProject project) {
		super(parent, style);
		setLayout(new GridLayout(1, true));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table = new Tree(this, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);

		table.setLayout(new GridLayout(1, true));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.project = project;
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public TreeItem[] getItems() {
		return table.getItems();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#pack(boolean)
	 */
	@Override
	public void pack(boolean changed) {
		if (changed) {
			RootContainer root = RootContainer.getRoot(project);
			root.update();
			table.clearAll(true);
			table.setItemCount(0);
			if (tableSelectionListener != null) {
				table.removeSelectionListener(tableSelectionListener);
			}
			setupClasspathInfo(table, root);
			makeChecksConsistentWithChildren();
			expandAll(table.getItems());
			if (tableSelectionListener == null) {
				tableSelectionListener = new TableSelectionListener();
				table.addSelectionListener(tableSelectionListener);
			}
		}
		table.pack(changed);
		super.pack(changed);
	}

	/**
	 * Expand all.
	 *
	 * @param items the items
	 */
	private void expandAll(TreeItem[] items) {
		for (TreeItem ti : items) {
			ti.setExpanded(true);
			expandAll(ti.getItems());
		}

	}

	/**
	 * Setup classpath info.
	 *
	 * @param tree the tree
	 * @param cp the cp
	 * @return the tree item
	 */
	private TreeItem setupClasspathInfo(Tree tree, ICPEType cp) {
		if (cp.isAvailable()) {
			TreeItem ti = new TreeItem(tree, SWT.NONE);
			setUpTreeItem(cp, ti);
			return populateChildren(cp, ti);
		}
		return null;
	}

	/**
	 * Setup classpath info.
	 *
	 * @param treeItem the tree item
	 * @param cp the cp
	 * @return the tree item
	 */
	private TreeItem setupClasspathInfo(TreeItem treeItem, ICPEType cp) {
		if (cp.isAvailable()) {
			TreeItem ti = new TreeItem(treeItem, SWT.NONE);
			setUpTreeItem(cp, ti);
			return populateChildren(cp, ti);
		}
		return null;
	}

	/**
	 * Populate children.
	 *
	 * @param parentClasspath the parent classpath
	 * @param parentTreeItem the parent tree item
	 * @return the tree item
	 */
	private TreeItem populateChildren(ICPEType parentClasspath,
			TreeItem parentTreeItem) {
		for (ICPEType child : parentClasspath.getChildren()) {
			setupClasspathInfo(parentTreeItem, child);
		}
		return parentTreeItem;
	}

	/**
	 * Sets the up tree item.
	 *
	 * @param cp the cp
	 * @param checked the checked
	 */
	private void setUpTreeItem(ICPEType cp, TreeItem checked) {
		checked.setText(cp.toString());
		checked.setChecked(cp.isEnabled());
		checked.setData(cp);
		if (cp.getIcon() != null) {
			checked.setImage(cp.getIcon());
		}

	}
}
