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

/**
 * The Class LibraryTableComposite is the container class for an org.eclipse.swt.widgets.Tree. It is designed so that
 * you should be able to toss it pretty much anywhere in an SWT environment.
 */
public class LibraryTableComposite extends Composite {

	/**
	 * The listener interface for receiving tableSelection events. The class that is interested in processing a
	 * tableSelection event implements this interface, and the object created with that class is registered with a
	 * component using the component's <code>addTableSelectionListener<code> method. When
	 * the tableSelection event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see TableSelectionEvent
	 */
	private final class TableSelectionListener implements SelectionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
		}

		/*
		 * (non-Javadoc)
		 * 
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
					updateChildCheckboxes(tree);
					makeChecksConsistentWithChildren();
				} catch (ClassCastException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private IProject project;

	private Tree table;

	private TableSelectionListener tableSelectionListener;

	/**
	 * Instantiates a new library table composite, setting up the tree along the way.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 * @param project
	 *            the project
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
	 * Expand all items in the tree.
	 * 
	 * @param items
	 *            the items
	 */
	private void expandAll(TreeItem[] items) {
		for (TreeItem ti : items) {
			ti.setExpanded(true);
			expandAll(ti.getItems());
		}

	}

	/**
	 * Gets the tree items in the table.
	 * 
	 * @return the items
	 */
	public TreeItem[] getItems() {
		return table.getItems();
	}

	/**
	 * Make checks marks consistent with children.
	 */
	private void makeChecksConsistentWithChildren() {
		for (TreeItem item : table.getItems()) {
			makeChecksConsistentWithChildren(item);
		}
	}

	/**
	 * Make check marks consistent with children.
	 * 
	 * @param item
	 *            the item
	 * @return null if checkbox should be gray, true if checked, false if unchecked
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

	/*
	 * (non-Javadoc)
	 * 
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
	 * Populate children of the tree with the given ICPEType
	 * 
	 * @param parentClasspath
	 *            the parent classpath
	 * @param parentTreeItem
	 *            the parent tree item
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
	 * Setup the classpath info tree items
	 * 
	 * @param tree
	 *            the tree
	 * @param cp
	 *            the cp
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
	 * Setup the classpath info tree items
	 * 
	 * @param treeItem
	 *            the tree item
	 * @param cp
	 *            the cp
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
	 * Sets the up tree item.
	 * 
	 * @param cp
	 *            the cp
	 * @param checked
	 *            the checked
	 */
	private void setUpTreeItem(ICPEType cp, TreeItem checked) {
		checked.setText(cp.toString());
		checked.setChecked(cp.isEnabled());
		checked.setData(cp);
		if (cp.getIcon() != null) {
			checked.setImage(cp.getIcon());
		}

	}

	/**
	 * Update all the children TreeItems of the given parent to be the same check as the parent.
	 * 
	 * @param parent
	 *            the parent
	 */
	protected void updateChildCheckboxes(TreeItem parent) {
		for (TreeItem child : parent.getItems()) {
			child.setChecked(parent.getChecked());
			child.setGrayed(false);
			updateChildCheckboxes(child);
		}
	}
}
