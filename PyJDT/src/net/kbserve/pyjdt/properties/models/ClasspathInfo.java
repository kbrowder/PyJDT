package net.kbserve.pyjdt.properties.models;

import org.eclipse.jdt.core.IClasspathEntry;

public class ClasspathInfo extends ClasspathContainer implements IClasspathInfo {
	private String path;
	private boolean enabled = true;
	private int entryKind = 0;

	public ClasspathInfo() {
	}

	public ClasspathInfo(IClasspathEntry classpathEntry) {
		setPath(classpathEntry.getPath().toPortableString());
		setEntryKind(classpathEntry.getEntryKind());
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return compareTo(obj) == 0;
	}

	@Override
	public int compareTo(Object obj) {
		if (obj instanceof IClasspathInfo) {
			return this.compareTo(((IClasspathInfo) obj).getPath());
		} else if (obj instanceof IClasspathEntry) {
			return this.compareTo(((IClasspathEntry) obj).getPath()
					.toPortableString());
		}
		return getPath().compareTo(obj.toString());

	}

	@Override
	public int getEntryKind() {
		return entryKind;
	}

	@Override
	public void setEntryKind(int type) {
		this.entryKind = type;
	}

}
