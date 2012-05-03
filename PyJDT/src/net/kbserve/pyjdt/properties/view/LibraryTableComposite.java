package net.kbserve.pyjdt.properties.view;

import net.kbserve.pyjdt.properties.models.IJDTClasspathContainer;
import net.kbserve.pyjdt.properties.models.RootContainer;

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

			table.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					if (arg0.detail == SWT.CHECK) {
						System.out.println("Selection: " + arg0.item);
						try {
							TreeItem tree = (TreeItem) arg0.item;
							IJDTClasspathContainer cpc = (IJDTClasspathContainer) tree
									.getData();
							cpc.setEnabled(tree.getChecked());
							cpc.setNoPrefererence(tree.getGrayed());
							tree.setExpanded(false);
						} catch (ClassCastException e) {
						}
					}

				}
			});

			setupClasspathInfo(table, root);

			boolean enabled = root.isNoPreference() || root.isEnabled();
			table.setEnabled(enabled);
		}
		table.pack(changed);
		super.pack(changed);
	}

	private void setupClasspathInfo(Tree tree, IJDTClasspathContainer cp) {
		TreeItem ti = new TreeItem(tree, SWT.NONE);
		setUpTreeItem(cp, ti);
		for (IJDTClasspathContainer child : cp.getChildren()) {
			setupClasspathInfo(ti, child);
		}
	}

	private void setupClasspathInfo(TreeItem treeItem, IJDTClasspathContainer cp) {
		TreeItem ti = new TreeItem(treeItem, SWT.NONE);
		setUpTreeItem(cp, ti);
		for (IJDTClasspathContainer child : cp.getChildren()) {
			setupClasspathInfo(ti, child);
		}
	}

	private void setUpTreeItem(IJDTClasspathContainer cp, TreeItem ti) {
		ti.setText(cp.toString());
		ti.setChecked(cp.isEnabled());
		ti.setGrayed(cp.isNoPreference());
		ti.setData(cp);
	}

}
