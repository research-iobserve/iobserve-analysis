package org.iobserve.planning.peropteryx.plugin;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class ModelFileHelper {

	public static IPath getModelFilePath(String modelDir, String extension) {
		IPath modelDirPath = new Path(modelDir);

		for (File file : modelDirPath.toFile().listFiles()) {
			if (getFileExtension(file.getName()).equals(extension)) {
				return new Path(file.getPath());
			}
		}

		return null;
	}

	/**
	 * Get file extension of the given path.
	 *
	 * @param path
	 *            path
	 * @return file extension
	 */
	private static String getFileExtension(final String path) {
		return path.substring(path.lastIndexOf(".") + 1, path.length());
	}
}
