package net.kbserve.pyjdt.properties;

public class ClasspathInfo implements IClasspathInfo {
	//TODO: support containers
	private String path;
	private boolean enabled = true; 
	private ClasspathInfo() {}
	
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
	
}
