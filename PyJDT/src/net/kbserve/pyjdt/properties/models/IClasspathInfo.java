package net.kbserve.pyjdt.properties.models;



public interface IClasspathInfo extends Comparable<Object>, IClasspathContainer {
	public String getPath();
	public void setPath(String path);
	public boolean isEnabled();
	public void setEnabled(boolean enabled);
	public int getEntryKind();
	public void setEntryKind(int entryKind);
}
