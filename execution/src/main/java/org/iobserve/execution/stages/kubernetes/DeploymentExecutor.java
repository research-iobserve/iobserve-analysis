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

import java.util.Map;

import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.execution.stages.IExecutor;
import org.iobserve.model.correspondence.AbstractEntry;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
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
public class DeploymentExecutor implements IExecutor<DeployComponentAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentExecutor.class);

    private final Map<String, Deployment> podsToDeploy;
    private final CorrespondenceModel correspondenceModel;

    public DeploymentExecutor(final Map<String, Deployment> podsToDeploy,
            final CorrespondenceModel correspondenceModel) {
        this.podsToDeploy = podsToDeploy;
        this.correspondenceModel = correspondenceModel;
    }

    @Override
    public void execute(final DeployComponentAction action) {
        DeploymentExecutor.LOGGER.info("Executing deployment");

        final KubernetesClient client = new DefaultKubernetesClient();
        final String rcName = action.getTargetAllocationContext().getResourceContainer_AllocationContext()
                .getEntityName().toLowerCase();
        final Deployment deployment = client.extensions().deployments().withName(rcName).get();
        final AssemblyContext targetAssemblyContext = action.getTargetAllocationContext()
                .getAssemblyContext_AllocationContext();
        String imageName = "";

        // Get the image name from the correspondence model
        for (final AbstractEntry e : this.correspondenceModel.getParts().get(0).getEntries()) {
            if (e instanceof AssemblyEntry) {
                final AssemblyEntry assEntry = (AssemblyEntry) e;
                if (assEntry.getAssembly().equals(targetAssemblyContext)) {
                    imageName = assEntry.getImplementationId();
                }
            }
        }

        if (deployment != null) {
            // Increase number of replicas if pod is already deployed...
            final int replicas = deployment.getSpec().getReplicas();
            deployment.getSpec().setReplicas(replicas + 1);

            DeploymentExecutor.LOGGER
                    .info("Scaled pod deployment of " + deployment.getMetadata().getName() + " to " + (replicas + 1));
        } else {
            // ... deploy new pod if this is not the case
            final Deployment newDeployment = this.podsToDeploy.get(rcName);
            final String imageLocator = newDeployment.getSpec().getTemplate().getSpec().getContainers().get(0)
                    .getImage();

            newDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(imageLocator + imageName);

            final Deployment result = client.extensions().deployments().inNamespace("default").create(newDeployment);

            DeploymentExecutor.LOGGER.info("Created new pod deployment " + result.getMetadata().getName());
        }

        client.close();
    }

}
