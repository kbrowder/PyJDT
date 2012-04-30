package net.kbserve.pyjdt.properties.models;

public interface IPropertyAcceptor {
	public void accept(IClasspathVisitor visitor);
}