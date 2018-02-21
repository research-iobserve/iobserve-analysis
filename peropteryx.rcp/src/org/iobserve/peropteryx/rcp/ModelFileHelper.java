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
public class ModelFileHelper {

    /**
     * Creates a new Path for the given model inside the models root directory and returns it to the
     * caller.
     *
     * @param modelDir
     *            - The directory relative to the workspace root where the model is stored. May end
     *            with a '/' or without.
     * @param fileExtension
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
        }
        return null;
    }
}
