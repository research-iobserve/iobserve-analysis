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
package org.iobserve.newplanning;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * A comment.
 *
 * @author LarsBlumke
 *
 */
public class ModelFileHelper {
    private static final Logger LOG = LogManager.getLogger(ModelFileHelper.class);

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
        }
        return null;
    }
}
