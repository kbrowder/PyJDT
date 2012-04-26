package net.kbserve.pyjdt.properties.view;

import net.kbserve.pyjdt.properties.IClasspathInfo;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class ClasspathInfoTableItem extends TableItem {

	private IClasspathInfo classpathInfo;
	public ClasspathInfoTableItem(Table parent, int style, IClasspathInfo classpathInfo) {
		super(parent, style);
		setClasspathInfo(classpathInfo);
		setText(0, getClasspathInfo().getPath());
		setChecked(getClasspathInfo().isEnabled());
	}
	public IClasspathInfo getClasspathInfo() {
		return classpathInfo;
	}
	public void setClasspathInfo(IClasspathInfo classpathInfo) {
		this.classpathInfo = classpathInfo;
	}
	@Override
	protected void checkSubclass() {
	}

}
