package net.kbserve.pyjdt;

import net.kbserve.pyjdt.properties.models.ICPEType;
import net.kbserve.pyjdt.properties.models.RootContainer;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.JUnitCore;
import org.junit.AfterClass;
import org.junit.Assert;
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
	public void testFindsContainer() {
		IProject project = TestUtilities.getProject("TestProject2");
		RootContainer rc = RootContainer.getRoot(project);
		Assert.assertNotNull("Could not find the JUnit container",rc.getChild(JUnitCore.JUNIT4_CONTAINER_PATH.toPortableString()));
	}
	
	@Test
	public void testFindsJarInContainer() {
		IProject project = TestUtilities.getProject("TestProject2");
		RootContainer rc = RootContainer.getRoot(project);
		ICPEType junitContainer = rc.getChild(JUnitCore.JUNIT4_CONTAINER_PATH.toPortableString());
		Assert.assertTrue("The container contains no classpaths !?!", junitContainer.getChildren().size()>0);
		
	}

	@AfterClass
	public static void tearDownAllTestProjects() throws Exception {
		TestUtilities.deleteAllTestProjects();
	}

}
