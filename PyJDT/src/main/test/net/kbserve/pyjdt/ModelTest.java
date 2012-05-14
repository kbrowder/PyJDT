package net.kbserve.pyjdt;

import static org.junit.Assert.fail;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.JUnitCore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelTest {
	@BeforeClass
	public static void setUpTestProject1() throws Exception {
		String projectName = "TestProject1";
		TestUtilities.createJDTProject(projectName);
	}

	@BeforeClass
	public static void setUpTestProject2() throws Exception {
		String projectName = "TestProject2";
		IJavaProject project;
		project = TestUtilities.createJDTProject(projectName);
		TestUtilities.addLibraryContainer(JUnitCore.JUNIT4_CONTAINER_PATH,
				project);
		TestUtilities.addPyDevNature(project.getProject(), "src");

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
