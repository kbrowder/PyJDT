package net.kbserve.pyjdt.tests;

import static org.junit.Assert.fail;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Test;

public class SerializationTest {
	 
	@Test
	public void test() {
		Testing t = new Testing();
		t.p("foo");
		t.p("bar");
		t.p("baz");
		File f = new File("tst.xml");
		try {
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(f)));
			e.writeObject(t);
			e.close();
			XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(f)));
			Testing t2 = (Testing)d.readObject();
			t2.getFoo().equals(t.getFoo());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			fail(e.getMessage().toString());
		}
	}

}
