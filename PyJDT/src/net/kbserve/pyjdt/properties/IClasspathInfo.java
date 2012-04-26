package net.kbserve.pyjdt.properties;


public interface IClasspathInfo {
	public String getPath();
	public void setPath(String path);
	public boolean isEnabled();
	public void setEnabled(boolean enabled);
}
