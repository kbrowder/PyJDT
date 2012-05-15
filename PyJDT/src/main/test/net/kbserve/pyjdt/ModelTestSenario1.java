package net.kbserve.pyjdt;

import net.kbserve.pyjdt.properties.models.CPEProject;
import net.kbserve.pyjdt.properties.models.ICPEType;
import net.kbserve.pyjdt.properties.models.RootContainer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.JUnitCore;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelTestSenario1 {


	@BeforeClass
	public static void setupProjects() throws Exception {
		TestUtilities.createJDTProject("TestProject1");
		
		String projectName = "TestProject2";
		IJavaProject project;
		project = TestUtilities.createJDTProject(projectName);
		TestUtilities.addLibraryContainer(JUnitCore.JUNIT4_CONTAINER_PATH,
				project);
		TestUtilities.addPyDevNature(project.getProject(), "src");


		projectName = "TestProject3";
		project = TestUtilities.createJDTProject(projectName);
		TestUtilities.addProject(project, new Path("/TestProject2"));
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
		Assert.assertTrue("The container contains no classpaths!", junitContainer.getChildren().size()>0);
		boolean foundJar = false;
		for(ICPEType child: junitContainer.getChildren()) {
			if(child.getPath().endsWith(".jar")) {
				foundJar = true;
				break;
			}
		}
		Assert.assertTrue("The classpath container contains no jar", foundJar);
	}
	
	@Test
	public void testFindSourceInPath() {
		IProject project = TestUtilities.getProject("TestProject3");
		RootContainer rc = RootContainer.getRoot(project);
		boolean foundProject = false;
		for(ICPEType child: rc.getChildren()) {
			if(child instanceof CPEProject) {
				if(child.getPath().endsWith("TestProject2")) {
					foundProject = true;
					break;
				}
			}
		}
		Assert.assertTrue("TestProject2/src not a classpath entry for TestProject3", foundProject);
	}

	@AfterClass
	public static void tearDownAllTestProjects() throws Exception {
		TestUtilities.deleteAllTestProjects();
	}

}
