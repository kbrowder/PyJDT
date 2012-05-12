package net.kbserve.pyjdt;

import static org.junit.Assert.fail;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.JUnitCore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelTest {
	@BeforeClass
	public static void setUpTestProject1() {
		
		String projectName = "TestProject1";
		try {
			TestUtilities.createJDTProject(projectName);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}		
	}
	
	@BeforeClass
	public static void setUpTestProject2() {
		String projectName = "TestProject2";
		IJavaProject project;
		try {
			project = TestUtilities.createJDTProject(projectName);
			TestUtilities.addLibraryContainer(JUnitCore.JUNIT4_CONTAINER_PATH, project);
			TestUtilities.addPyDevNature(project.getProject(), "src");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@AfterClass
	public static void tearDownAllTestProjects() throws Exception {
		TestUtilities.deleteAllTestProjects();
	}

}
