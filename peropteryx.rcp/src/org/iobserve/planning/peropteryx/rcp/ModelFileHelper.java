package org.iobserve.planning.peropteryx.rcp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class ModelFileHelper {
	private static final Logger LOG = LogManager.getLogger(ModelFileHelper.class);

	public static IPath getModelFilePath(final String modelDir, final String extension) {
		IPath modelDirPath = new Path(modelDir);
		IFolder modelDirFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(modelDirPath);

		try {
			for (IResource resource : modelDirFolder.members()) {
				if ((resource.getType() == IResource.FILE) && resource.getFileExtension().equals(extension)) {
					return new Path("platform:/resource/" + resource.getFullPath().toString());
				}
			}
		} catch (CoreException e) {
		}
		return null;
	}
}
