/********************************************************************************
 *  
 * 
 *  @author Kevin Browder
 * 
 *  Copyright (c) 2012, Kevin Browder All rights reseved
 * 
 *  This file is part of PyJDT
 *  
 *  PyJDT is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *******************************************************************************/
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
