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
package org.iobserve.peropteryx.rcp;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Helper class for resolving model files.
 *
 * @author Tobias Pöppke
 * @author Philipp Weimann
 * @author Lars Bluemke (gradle integration)
 *
 */
public final class ModelFileHelper {

    private ModelFileHelper() {

    }

    /**
     * Creates a new Path for the given model inside the models root directory and returns it to the
     * caller.
     *
     * @param modelDir
     *            - The directory relative to the workspace root where the model is stored. May end
     *            with a '/' or without.
     * @param extension
     *            - The file extension of the specific model including a dot, e.g. .allocation for
     *            an allocation model file.
     * @return Model file path specific to eclipse platform
     */
    public static IPath getModelFilePath(final String modelDir, final String extension) {
        final IPath modelDirPath = new Path(modelDir);
        final IFolder modelDirFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(modelDirPath);

        try {
            for (final IResource resource : modelDirFolder.members()) {
                if ((resource.getType() == IResource.FILE) && resource.getFileExtension().equals(extension)) {
                    return new Path("platform:/resource/" + resource.getFullPath().toString());
                }
            }
        } catch (final CoreException e) {
            e.printStackTrace();
        }
        return null;
    }
}
