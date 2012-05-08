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

public class LibraryTableComposite extends Composite {

	private final class TableSelectionListener implements SelectionListener {
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

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
		}
	}

	protected void updateAll(TreeItem parent, boolean checked) {
		for (TreeItem child : parent.getItems()) {
			child.setChecked(checked);
			child.setGrayed(false);
			updateAll(child, checked);
		}
	}

	/**
	 * 
	 * @param child
	 * @return null if checkbox should be gray, true if checked, false if
	 *         unchecked
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
	
	private void makeChecksConsistentWithChildren() {
		for(TreeItem item: table.getItems()) {
			makeChecksConsistentWithChildren(item);
		}
	}

	private IProject project;
	private Tree table;
	private TableSelectionListener tableSelectionListener;

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

	public TreeItem[] getItems() {
		return table.getItems();
	}

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

	private void expandAll(TreeItem[] items) {
		for (TreeItem ti : items) {
			ti.setExpanded(true);
			expandAll(ti.getItems());
		}

	}

	private TreeItem setupClasspathInfo(Tree tree, ICPEType cp) {
		if (cp.isAvailable()) {
			TreeItem ti = new TreeItem(tree, SWT.NONE);
			setUpTreeItem(cp, ti);
			return populateChildren(cp, ti);
		}
		return null;
	}

	private TreeItem setupClasspathInfo(TreeItem treeItem, ICPEType cp) {
		if (cp.isAvailable()) {
			TreeItem ti = new TreeItem(treeItem, SWT.NONE);
			setUpTreeItem(cp, ti);
			return populateChildren(cp, ti);
		}
		return null;
	}

	private TreeItem populateChildren(ICPEType parentClasspath,
			TreeItem parentTreeItem) {
		for (ICPEType child : parentClasspath.getChildren()) {
			setupClasspathInfo(parentTreeItem, child);
			if (!parentClasspath.isEnabled() && child.isEnabled()) {
				parentTreeItem.setGrayed(true);
			} else if (parentClasspath.isEnabled() && !child.isEnabled()) {
				parentTreeItem.setGrayed(true);
			}
		}
		return parentTreeItem;
	}

	private void setUpTreeItem(ICPEType cp, TreeItem checked) {
		checked.setText(cp.toString());
		checked.setChecked(cp.isEnabled());
		checked.setData(cp);
	}

}
