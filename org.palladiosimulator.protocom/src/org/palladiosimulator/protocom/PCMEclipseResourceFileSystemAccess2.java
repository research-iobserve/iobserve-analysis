package org.palladiosimulator.protocom;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.AbstractFileSystemAccess2;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.ui.resource.ProjectByResourceProvider;
import org.eclipse.xtext.util.RuntimeIOException;
import org.eclipse.xtext.util.StringInputStream;

import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;

public class PCMEclipseResourceFileSystemAccess2 extends AbstractFileSystemAccess2 {

    private final static Logger log = Logger.getLogger(PCMEclipseResourceFileSystemAccess2.class);

    /**
     * @noimplement This interface is not intended to be implemented by clients.
     */
    public interface IFileCallback {
        public void afterFileUpdate(IFile file);

        public void afterFileCreation(IFile file);

        /**
         * @return whether a deletion is vetoed.
         */
        public boolean beforeFileDeletion(IFile file);
    }

    private IProject project;

    private IProgressMonitor monitor;

    private IFileCallback callBack;

    @Inject
    private ProjectByResourceProvider projectProvider;

    private Multimap<URI, IPath> sourceTraces;

    /**
     * @since 2.4
     */
    protected Multimap<URI, IPath> getSourceTraces() {
        return sourceTraces;
    }

    /**
     * @since 2.4
     */
    protected void resetSourceTraces() {
        sourceTraces = null;
    }

    /**
     * @since 2.3
     */
    protected IFileCallback getCallBack() {
        return callBack;
    }

    public void setProject(IProject project) {
        this.project = project;
    }

    @Override
    public void setContext(Object context) {
        if (context instanceof IProject) {
            setProject((IProject) context);
        } else if (context instanceof Resource) {
            Resource resource = (Resource) context;
            IProject project = getProjectContext(resource);
            if (project != null) {
                setProject(project);
            }
        } else {
            throw new IllegalArgumentException("Couldn't handle context " + context);
        }
    }

    /**
     * @since 2.8
     */
    protected IProject getProjectContext(Resource resource) {
        return projectProvider.getProjectContext(resource);
    }

    /**
     * @since 2.6
     */
    protected IProject getProject() {
        return project;
    }

    public void setMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }

    public void setPostProcessor(IFileCallback callBack) {
        this.callBack = callBack;
    }

    protected IProgressMonitor getMonitor() {
        return monitor;
    }

    /**
     * @since 2.4
     */
    protected boolean ensureOutputConfigurationDirectoryExists(OutputConfiguration outputConfig) {
        IContainer container = getContainer(outputConfig);
        if (!container.exists()) {
            if (outputConfig.isCreateOutputDirectory()) {
                try {
                    createContainer(container);
                    return true;
                } catch (CoreException e) {
                    throw new RuntimeIOException(e);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void generateFile(String fileName, String outputName, CharSequence contents) {
        if (monitor.isCanceled())
            throw new OperationCanceledException();
        OutputConfiguration outputConfig = getOutputConfig(outputName);

        if (!ensureOutputConfigurationDirectoryExists(outputConfig))
            return;

        IFile file = getFile(fileName, outputName);
        try {
            String encoding = getEncoding(file);
            CharSequence postProcessedContent = postProcess(fileName, outputName, contents, encoding);
            String contentsAsString = postProcessedContent.toString();
            if (file.exists()) {
                if (outputConfig.isOverrideExistingResources()) {
                    StringInputStream newContent = getInputStream(contentsAsString, encoding);
                    if (hasContentsChanged(file, newContent)) {
                        // reset to offset zero allows to reuse internal byte[]
                        // no need to convert the string twice
                        newContent.reset();
                        file.setContents(newContent, true, outputConfig.isKeepLocalHistory(), monitor);
                    } else {
                        file.touch(getMonitor());
                    }
                    if (file.isDerived() != outputConfig.isSetDerivedProperty()) {
                        setDerived(file, outputConfig.isSetDerivedProperty());
                    }
                    if (callBack != null)
                        callBack.afterFileUpdate(file);
                }
            } else {
                ensureParentExists(file);
                file.create(getInputStream(contentsAsString, encoding), true, monitor);
                if (outputConfig.isSetDerivedProperty()) {
                    setDerived(file, true);
                }
                if (callBack != null)
                    callBack.afterFileCreation(file);
            }
        } catch (CoreException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * @since 2.4
     */
    @Override
    public void generateFile(String fileName, String outputName, InputStream content) {
        if (monitor.isCanceled())
            throw new OperationCanceledException();
        OutputConfiguration outputConfig = getOutputConfig(outputName);

        if (!ensureOutputConfigurationDirectoryExists(outputConfig))
            return;

        try {
            IFile file = getFile(fileName, outputName);
            if (file.exists()) {
                if (outputConfig.isOverrideExistingResources()) {
                    if (hasContentsChanged(file, content)) {
                        // reset to offset zero allows to reuse internal byte[]
                        // no need to convert the string twice
                        content.reset();
                        file.setContents(content, true, outputConfig.isKeepLocalHistory(), monitor);
                    } else {
                        file.touch(getMonitor());
                    }
                    if (file.isDerived() != outputConfig.isSetDerivedProperty())
                        setDerived(file, outputConfig.isSetDerivedProperty());
                    if (callBack != null)
                        callBack.afterFileUpdate(file);
                }
            } else {
                ensureParentExists(file);
                file.create(content, true, monitor);
                if (outputConfig.isSetDerivedProperty()) {
                    setDerived(file, true);
                }
                if (callBack != null)
                    callBack.afterFileCreation(file);
            }
        } catch (CoreException e) {
            throw new RuntimeIOException(e);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * @since 2.3
     */
    protected String getEncoding(IFile file) throws CoreException {
        return file.getCharset(true);
    }

    /**
     * @since 2.3
     */
    @SuppressWarnings("deprecation")
    protected void setDerived(IFile file, boolean derived) throws CoreException {
        file.setDerived(derived);
    }

    /**
     * @deprecated use {@link #createContainer(IContainer)} instead
     */
    @Deprecated
    protected void createFolder(IFolder folder) throws CoreException {
        ensureExists(folder);
    }

    /**
     * @since 2.4
     */
    protected void createContainer(IContainer container) throws CoreException {
        if (container instanceof IFolder) {
            createFolder((IFolder) container);
        } else {
            ensureExists(container);
        }
    }

    protected void ensureParentExists(IFile file) throws CoreException {
        if (!file.exists()) {
            ensureExists(file.getParent());
        }
    }

    protected void ensureExists(IContainer container) throws CoreException {
        if (container.exists())
            return;
        if (container instanceof IFolder) {
            ensureExists(container.getParent());
            ((IFolder) container).create(true, true, monitor);
        }
    }

    protected StringInputStream getInputStream(String contentsAsString, String encoding) {
        try {
            return new StringInputStream(contentsAsString, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * @deprecated use {@link #getContainer(OutputConfiguration)} instead
     */
    @Deprecated
    protected IFolder getFolder(OutputConfiguration outputConfig) {
        return (IFolder) getContainer(outputConfig);
    }

    /**
     * @since 2.4
     */
    protected IContainer getContainer(OutputConfiguration outputConfig) {
        String path;
        if (getCurrentSource() == null) {
            path = outputConfig.getOutputDirectory();
        } else {
            path = outputConfig.getOutputDirectory(getCurrentSource());
        }
        if (".".equals(path) || "./".equals(path) || "".equals(path)) {
            return project;
        }
        return project.getFolder(new Path(path));
    }

    protected boolean hasContentsChanged(IFile file, StringInputStream newContent) {
        return hasContentsChanged(file, (InputStream) newContent);
    }

    /**
     * @since 2.4
     */
    protected boolean hasContentsChanged(IFile file, InputStream newContent) {
        boolean contentChanged = false;
        BufferedInputStream oldContent = null;
        try {
            oldContent = new BufferedInputStream(file.getContents());
            int newByte = newContent.read();
            int oldByte = oldContent.read();
            while (newByte != -1 && oldByte != -1 && newByte == oldByte) {
                newByte = newContent.read();
                oldByte = oldContent.read();
            }
            contentChanged = newByte != oldByte;
        } catch (CoreException e) {
            contentChanged = true;
        } catch (IOException e) {
            contentChanged = true;
        } finally {
            if (oldContent != null) {
                try {
                    oldContent.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return contentChanged;
    }

    /**
     * @since 2.3
     */
    @Deprecated
    protected IFile getSmapFile(IFile javaSourceFile) {
        log.warn("Smap files are no longer generated on disk.");
        return null;
    }

    /**
     * Can be used to announce that a builder participant is done with this file system access and
     * all potentially recorded trace information should be persisted.
     * 
     * @param generatorName
     *            the name of the generator.
     * @since 2.3
     */
    public void flushSourceTraces(String generatorName) throws CoreException {
        sourceTraces = null;
    }

    private void syncIfNecessary(IFile result, IProgressMonitor progressMonitor) {
        try {
            result.refreshLocal(IResource.DEPTH_ZERO, progressMonitor);
        } catch (CoreException c) {
            // ignore
        }
    }

    @Override
    public void deleteFile(String fileName, String outputName) {
        try {
            IFile file = getFile(fileName, outputName);
            deleteFile(file, outputName, monitor);
        } catch (CoreException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * @since 2.3
     */
    public void deleteFile(IFile file, IProgressMonitor monitor) throws CoreException {
        deleteFile(file, DEFAULT_OUTPUT, monitor);
    }

    /**
     * @since 2.5
     */
    public void deleteFile(IFile file, String outputName, IProgressMonitor monitor) throws CoreException {
        OutputConfiguration outputConfig = getOutputConfig(outputName);
        IFileCallback callBack = getCallBack();
        if ((callBack == null || callBack.beforeFileDeletion(file)) && file.exists()) {
            file.delete(outputConfig.isKeepLocalHistory() ? IResource.KEEP_HISTORY : IResource.NONE, monitor);
        }
    }

    protected IFile getFile(String fileName, String outputName) {
        return getFile(fileName, outputName, monitor);
    }

    /**
     * @since 2.7
     */
    protected IFile getFile(String fileName, String outputName, IProgressMonitor progressMonitor) {
        OutputConfiguration configuration = getOutputConfig(outputName);
        IContainer container = getContainer(configuration);
        IFile result = container.getFile(new Path(fileName));
        syncIfNecessary(result, progressMonitor);
        return result;
    }

    /**
     * @since 2.7
     */
    public URI getURI(String path, IProgressMonitor progressMonitor) {
        return getURI(path, DEFAULT_OUTPUT, progressMonitor);
    }

    /**
     * We cannot use the storage to URI mapper here, as it only works for Xtext based languages
     * 
     * @since 2.3
     */
    @Override
    public URI getURI(String path, String outputConfiguration) {
        return getURI(path, outputConfiguration, monitor);
    }

    /**
     * @since 2.7
     */
    public URI getURI(String path, String outputName, IProgressMonitor progressMonitor) {
        OutputConfiguration configuration = getOutputConfig(outputName);
        IContainer container = getContainer(configuration);
        IPath childPath = container.getFullPath().append(path).makeAbsolute();
        return URI.createPlatformResourceURI(childPath.toString(), true);
    }

    /**
     * @since 2.7
     */
    public InputStream readBinaryFile(String fileName, IProgressMonitor progressMonitor) {
        return readBinaryFile(fileName, DEFAULT_OUTPUT, progressMonitor);
    }

    /**
     * @since 2.4
     */
    @Override
    public InputStream readBinaryFile(String fileName, String outputCfgName) throws RuntimeIOException {
        return readBinaryFile(fileName, outputCfgName, monitor);
    }

    /**
     * @since 2.7
     */
    public InputStream readBinaryFile(String fileName, String outputCfgName, IProgressMonitor progressMonitor)
            throws RuntimeIOException {
        try {
            IFile file = getFile(fileName, outputCfgName, progressMonitor);
            return new BufferedInputStream(file.getContents());
        } catch (CoreException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * @since 2.7
     */
    public CharSequence readTextFile(String fileName, IProgressMonitor progressMonitor) {
        return readTextFile(fileName, DEFAULT_OUTPUT, progressMonitor);
    }

    /**
     * @since 2.4
     */
    @Override
    public CharSequence readTextFile(String fileName, String outputCfgName) throws RuntimeIOException {
        return readTextFile(fileName, outputCfgName, monitor);
    }

    /**
     * @since 2.7
     */
    public CharSequence readTextFile(String fileName, String outputCfgName, IProgressMonitor progressMonitor)
            throws RuntimeIOException {
        try {
            IFile file = getFile(fileName, outputCfgName, progressMonitor);
            String encoding = getEncoding(file);
            InputStream inputStream = new BufferedInputStream(file.getContents());
            try {
                byte[] bytes = ByteStreams.toByteArray(inputStream);
                return new String(bytes, encoding);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        } catch (CoreException e) {
            throw new RuntimeIOException(e);
        }
    }

}
