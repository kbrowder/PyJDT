package net.kbserve.pyjdt;

import net.kbserve.pyjdt.properties.models.PersistentProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.python.pydev.plugin.nature.PythonNature;

public class JDTChangeListener implements IElementChangedListener {
	@Override
	public void elementChanged(ElementChangedEvent arg0) {
		IJavaElementDelta delta = arg0.getDelta();
		IJavaElement element = delta.getElement();

		IJavaProject jp = element.getJavaProject();
		if (jp != null) {
			updateClasspaths(jp.getProject());
		}

		parseDelta(delta);

		switch (delta.getKind()) {
		case IJavaElementDelta.ADDED:
			break;
		case IJavaElementDelta.CHANGED:
			break;
		case IJavaElementDelta.REMOVED:
			break;//TODO: do something here
		}

	}

	private void parseDelta(IJavaElementDelta delta) {
		IJavaElement element = delta.getElement();
		try {
			IJavaProject jp = element.getJavaProject();
			System.out.println("---" + element);
			if (jp != null) {
				System.out.println("-" + jp.getResolvedClasspath(false));
				for (IClasspathEntry cp : jp.getResolvedClasspath(true)) {
					// Might need to use getRawClasspath?
					System.out.println("resolved cp:\t"
							+ cp.getPath().toPortableString() + " = "
							+ cp.getEntryKind());
					PersistentProperties.load(jp.getProject()).getChildren(
							cp.getPath().toFile().getAbsolutePath());

				}

				PythonNature pythonNature = (PythonNature) jp.getProject()
						.getNature(PythonNature.PYTHON_NATURE_ID);
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}
		System.out.println("Changed: " + element + "=>" + element.getClass());
		for (IJavaElementDelta d : delta.getAffectedChildren()) {
			parseDelta(d);
		}
	}

	public static void updateClasspaths(IProject project) {// TODO: moveme
		IJavaProject jp = JavaCore.create(project);
		try {
			if (jp != null) {
				for (IClasspathEntry cp : jp.getRawClasspath()) {
					System.out.println("resolved cp:\t"
							+ cp.getPath().toFile().getAbsolutePath() + " = "
							+ cp.getEntryKind());
					System.out
							.println("\t\t" + cp.getPath().toPortableString());
					PersistentProperties.load(jp.getProject()).getOrCreateChildren(cp);

				}

				PythonNature pythonNature = (PythonNature) jp.getProject()
						.getNature(PythonNature.PYTHON_NATURE_ID);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

}