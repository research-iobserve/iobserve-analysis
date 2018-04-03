/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.planning;

import org.eclipse.core.runtime.CoreException;

/**
 * Main class for debugging.
 *
 * @author Lars Bluemke
 *
 */
public final class PerOpteryxDebugStart {

    private PerOpteryxDebugStart() {

    }

    public static void main(final String[] args) throws CoreException {
        // final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        //
        // final ILaunchConfigurationType type = manager
        // .getLaunchConfigurationType("org.eclipse.cdt.launch.applicationLaunchType");
        // final ILaunchConfiguration[] lcs = manager.getLaunchConfigurations(type);
        //
        // for (final ILaunchConfiguration iLaunchConfiguration : lcs) {
        // if (iLaunchConfiguration.getName().equals("peropteryx.product (1)")) {
        // final ILaunchConfigurationWorkingCopy t = iLaunchConfiguration.getWorkingCopy();
        // final ILaunchConfiguration config = t.doSave();
        // if (config != null) {
        // // config.launch(ILaunchManager.RUN_MODE, null);
        // config.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor(), true, true);
        // }
        // }
        // }

        // final String mode = "debug";
        // final IWorkspace workspace = ResourcesPlugin.getWorkspace();
        // final IWorkspaceRoot root = workspace.getRoot();
        // final IProject project = root.getProject("peropteryx.rcp");
        // final IFolder folder = project.getFolder("peropteryx.rcp");
        //
        // final IFile productFile = folder.getFile("peropteryx.product");
        // final IProductModel workspaceProductModel = new WorkspaceProductModel(productFile,
        // false);
        // try {
        // workspaceProductModel.load();
        // } catch (final CoreException e) {
        // PDEPlugin.log(e);
        // }
        // final IProduct product = workspaceProductModel.getProduct();
        // new LaunchAction(product, productFile.getFullPath().toOSString(), mode).run();

        // ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        // ILaunchConfiguration launchConfiguration =
        // launchManager.getLaunchConfiguration("peropteryx.rcp (1)");
        // ILaunchConfigurationWorkingCopy launchConfigWorkingCopy =
        // launchConfiguration.getWorkingCopy();
        //// launchConfigWorkingCopy.setAttributes(launchAttributes); // launchAttributes is a
        // Map<String,String>
        //// ILaunchConfiguration newLaunchConfig = launchConfigWorkingCopy.doSave();
        // launchConfigWorkingCopy.launch(ILaunchManager.RUN_MODE, new NullProgressMonitor(), true);
    }

}
