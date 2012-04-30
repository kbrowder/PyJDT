package net.kbserve.pyjdt.properties.models;

public interface IClasspathVisitor {
	public void visit(IClasspathInfo classpathInfo);
	public void visit(IPersistentProperties persistantProperties);
}
