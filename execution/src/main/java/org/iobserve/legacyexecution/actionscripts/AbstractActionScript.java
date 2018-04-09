/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.legacyexecution.actionscripts;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.inject.Module;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.RunScriptOnNodesException;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProvider;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class representing an execution script to be executed for a specific action.
 *
 * The actions derived from this class have to implement the {@link #execute()} method which
 * executes the corresponding action.
 *
 * @author Tobias Pöppke
 *
 */
public abstract class AbstractActionScript {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractActionScript.class);

    /** Data that is shared throughout the adaptation stage. */
    protected final AdaptationData data;

    /**
     * Constructs a new action script instance with the specified data.
     *
     * @param data
     *            data shared for adaptation stage
     */
    public AbstractActionScript(final AdaptationData data) {
        this.data = data;
    }

    /**
     * Executes this action.
     *
     * In case of an exception during the execution, the calling class has to signal the operator to
     * perform the task manually.
     *
     * @throws RunNodesException
     *             if there was an error acquiring or terminating a node
     * @throws RunScriptOnNodesException
     *             if there was an error with a script run on a group of nodes
     * @throws IOException
     *             if a needed script for this action could not be found
     */
    public abstract void execute() throws RunNodesException, RunScriptOnNodesException, IOException;

    /**
     * Indicates whether this action script is automatically executable or if it requires operator
     * interaction.
     *
     * Even if this method returns true, in an error case, there might still be the need for
     * operator intervention.
     *
     * @return true if the script does not need operator intervention
     */
    public abstract boolean isAutoExecutable();

    /**
     * Retrieves a description of the actions executed by this script.
     *
     * If an error occurs while performing the action, the operator will be presented with this
     * description to execute it manually.
     *
     * @return the description
     */
    public abstract String getDescription();

    /**
     * Returns a compute service client to use for further queries to the cloud provider.
     *
     * @param container
     *            the resource container for which to get the compute service
     * @return the corresponding compute service
     */
    protected ComputeService getComputeServiceForContainer(final ResourceContainerCloud container) {
        final CloudProvider provider = container.getInstanceType().getProvider();

        final ComputeServiceContext context = ContextBuilder.newBuilder(provider.getName())
                .credentials(provider.getIdentity(), provider.getCredential())
                .modules(ImmutableSet.<Module> of(new SLF4JLoggingModule(), new SshjSshClientModule())) // NOCS
                .buildView(ComputeServiceContext.class);

        final ComputeService client = context.getComputeService();
        return client;
    }

    /**
     * Reads the file contents of the given file and returns it as a String.
     *
     * @param fileURI
     *            the file to read
     * @return the contents of the file as a String
     * @throws IOException
     *             if there was an error with the file
     */
    protected String getFileContents(final URI fileURI) throws IOException {
        if (fileURI.isFile()) {
            return Files.asCharSource(new File(fileURI.toFileString()), StandardCharsets.UTF_8).read();
        } else {
            final String msg = String.format("File at '%s' is not a valid file! Path is probably wrong.",
                    fileURI.toFileString());
            AbstractActionScript.LOG.warn(msg);
            throw new IOException(msg);
        }
    }

    /**
     * Checks whether the given resource container is a cloud container and returns it as such.
     *
     * @param container
     *            the container to check
     * @return the cloud resource container
     * @throws IllegalArgumentException
     *             if the container was not a valid cloud container
     */
    protected ResourceContainerCloud getResourceContainerCloud(final ResourceContainer container)
            throws IllegalArgumentException {
        if (!(container instanceof ResourceContainerCloud)) {
            final String error = String.format(
                    "ResourceContainer '%s' was not a cloud container, therefore no action is possible on it!",
                    container.getEntityName());
            AbstractActionScript.LOG.error(error);
            throw new IllegalArgumentException(error);
        }

        final ResourceContainerCloud cloudContainer = (ResourceContainerCloud) container;
        return cloudContainer;
    }

    /**
     * Returns the name of the folder where artifacts of the given assembly context are stored.
     *
     * @param context
     *            the needed assembly context
     * @return the folder name for this context
     */
    protected String getAssemblyContextFolderName(final AssemblyContext context) {
        return context.getEncapsulatedComponent__AssemblyContext().getEntityName();
    }
}
