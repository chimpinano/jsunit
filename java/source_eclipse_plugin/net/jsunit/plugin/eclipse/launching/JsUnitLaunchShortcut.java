package net.jsunit.plugin.eclipse.launching;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.launching.SocketUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

public class JsUnitLaunchShortcut implements ILaunchShortcut {

	private ILaunchManager launchManager;

	public JsUnitLaunchShortcut() {
		launchManager = DebugPlugin.getDefault().getLaunchManager();
	}
	
	public void launch(ISelection selection, String mode) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		IFile file = (IFile) structuredSelection.getFirstElement();
		launch(file, mode);
	}

	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile) input.getAdapter(IFile.class);
		launch(file, mode);
	}

	private void launch(IFile file, String mode) {
		ILaunchConfiguration config = createConfiguration(file);
		DebugUITools.launch(config, mode);
	}

	private ILaunchConfiguration createConfiguration(IFile file) {
		ILaunchConfiguration config = null;
		try {
			ILaunchConfigurationType configType = jsUnitLaunchConfigType();
			String testPagePath = file.getProjectRelativePath().toString();
			ILaunchConfigurationWorkingCopy wc = configType.newInstance(
				null, launchManager.generateUniqueLaunchConfigurationNameFrom(testPagePath)
			);
			wc.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_TEST_PAGE_PATH, testPagePath);
			wc.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_PROJECT_NAME, file.getProject().getName());
			wc.setAttribute(JsUnitLaunchConfiguration.ATTRIBUTE_PORT, SocketUtil.findFreePort());
			config = wc.doSave();
		} catch (CoreException ce) {
			throw new RuntimeException(ce);
		}
		return config;
	}

	private ILaunchConfigurationType jsUnitLaunchConfigType() {
		return launchManager.getLaunchConfigurationType(JsUnitLaunchConfiguration.ID_JSUNIT_APPLICATION);		
	}

}