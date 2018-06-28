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
package org.iobserve.execution.stages.kubernetes;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.model.correspondence.AbstractEntry;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.provider.file.CorrespondenceModelHandler;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Deploys a component instance with kubernetes. If the pod is already running this is done by
 * incrementing its number of replicas. In case it is the first instance of this particular pod this
 * may also require the creation of a new pod from its specifications as mentioned in
 * {@link AllocationExecutor}.
 *
 * @author Lars Bluemke
 *
 */
public class DeploymentExecutor extends AbstractExecutor<DeployComponentAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentExecutor.class);

    private final Map<String, Deployment> podsToDeploy;
    private final File correspondenceModelFile;
    private final String namespace;

    // TODO is this the correct location for the executor's resourceSet
	private ResourceSet resourceSet = new ResourceSetImpl();

    /**
     * Creates a new deployment executor.
     *
     * @param podsToDeploy
     *            Blueprints of pods to deploy
     * @param correspondenceModelFile
     *            The correspondence model
     * @param namespace
     *            Kubernetes namespace
     */
    public DeploymentExecutor(final Map<String, Deployment> podsToDeploy, final File correspondenceModelFile,
            final String namespace) {
        this.podsToDeploy = podsToDeploy;
        this.correspondenceModelFile = correspondenceModelFile;
        this.namespace = namespace;
    }

    @Override
    public void execute(final DeployComponentAction action) {
        // Can't be loaded earlier because it references the other models received via TCP
        final CorrespondenceModel correspondenceModel = new CorrespondenceModelHandler(resourceSet)
                .load(URI.createFileURI(this.correspondenceModelFile.getAbsolutePath()));
        final KubernetesClient client = new DefaultKubernetesClient();
        final String rcName = this.normalizeComponentName(
                action.getTargetAllocationContext().getResourceContainer_AllocationContext().getEntityName());
        final Deployment deployment = client.extensions().deployments().inNamespace(this.namespace).withName(rcName)
                .get();
        final AssemblyContext targetAssemblyContext = action.getTargetAllocationContext()
                .getAssemblyContext_AllocationContext();
        final String imageName = this.getImageName(correspondenceModel, targetAssemblyContext);

        if (deployment != null) {
            // Increase number of replicas if pod is already deployed...
            final int replicas = deployment.getSpec().getReplicas();
            deployment.getSpec().setReplicas(replicas + 1);
            client.extensions().deployments().inNamespace(this.namespace).withName(rcName).replace(deployment);

            if (DeploymentExecutor.LOGGER.isDebugEnabled()) {
                DeploymentExecutor.LOGGER.debug(
                        "Scaled pod deployment of " + deployment.getMetadata().getName() + " to " + (replicas + 1));
            }
        } else {
            // ... deploy new pod if this is not the case
            final Deployment newDeployment = this.podsToDeploy.get(rcName);
            final String imageLocator = newDeployment.getSpec().getTemplate().getSpec().getContainers().get(0)
                    .getImage();

            newDeployment.getSpec().getTemplate().getSpec().getContainers().get(0)
                    .setImage(imageLocator + "/" + imageName);
            newDeployment.getSpec().getTemplate().getSpec().getContainers().get(0)
                    .setName(this.normalizeComponentName(targetAssemblyContext.getEntityName()));

            client.extensions().deployments().inNamespace(this.namespace).create(newDeployment);

            if (DeploymentExecutor.LOGGER.isDebugEnabled()) {
                DeploymentExecutor.LOGGER.debug("Image set to " + imageLocator + "/" + imageName);
                DeploymentExecutor.LOGGER.debug("Created new pod deployment " + newDeployment.getMetadata().getName());
            }
        }

        // Wait until deployed instance is ready
        while (!client.extensions().deployments().inNamespace(this.namespace).withName(rcName).isReady()) {
            if (DeploymentExecutor.LOGGER.isDebugEnabled()) {
                DeploymentExecutor.LOGGER.debug(rcName + " is not ready yet.");
            }
        }

        client.close();
    }

    /**
     * Gets the image name from the correspondence model.
     *
     * @param correspondenceModel
     *            The correspondence model containing the image name
     * @param targetAssemblyContext
     *            The assembly context whose image name is required
     * @return The image name
     */
    private String getImageName(final CorrespondenceModel correspondenceModel,
            final AssemblyContext targetAssemblyContext) {
        String imageName = "";

        for (final AbstractEntry e : correspondenceModel.getParts().get(0).getEntries()) {
            if (e instanceof AssemblyEntry) {
                final AssemblyEntry assEntry = (AssemblyEntry) e;

                if (assEntry.getAssembly().getId().equals(targetAssemblyContext.getId())) {
                    imageName = assEntry.getImplementationId();
                }
            }
        }

        return imageName;
    }
}
