package net.kbserve.pyjdt.tests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Testing {
	protected final Map<String, String> props = new HashMap<String, String>();
	public List<String> getFoo() {
		return new LinkedList(props.keySet());
	}
	public void setFoo(List<String> f) {
		for(String s: f) {
			props.put(s, s);
		}
	}
	public void p(String s) {
		props.put(s, s);
	}
} 