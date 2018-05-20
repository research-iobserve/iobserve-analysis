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

import org.iobserve.adaptation.executionplan.UndeployComponentAction;
import org.iobserve.execution.stages.IExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Undeployment with kubernetes by decrementing the pod's number of replicas.
 *
 * @author Lars Bluemke
 *
 */
public class UndeploymentExecutor implements IExecutor<UndeployComponentAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UndeploymentExecutor.class);

    private final String namespace;

    /**
     * Creates a new executor for undeployments.
     *
     * @param namespace
     *            Kubernetes namespace
     */
    public UndeploymentExecutor(final String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void execute(final UndeployComponentAction action) {
        final KubernetesClient client = new DefaultKubernetesClient();
        final String rcName = action.getTargetAllocationContext().getResourceContainer_AllocationContext()
                .getEntityName().toLowerCase();
        final Deployment deployment = client.extensions().deployments().inNamespace(this.namespace).withName(rcName)
                .get();
        final int replicas = deployment.getSpec().getReplicas();

        // Note: Decrementing to 0 deletes all pods but not the deployment in kubernetes. The
        // deployment is deleted in the DeallocationExecutor.
        if (replicas > 0) {
            deployment.getSpec().setReplicas(replicas - 1);
            client.extensions().deployments().inNamespace(this.namespace).withName(rcName).replace(deployment);
        }

        client.close();

        if (UndeploymentExecutor.LOGGER.isDebugEnabled()) {
            UndeploymentExecutor.LOGGER
                    .debug("Scaled pod deployment of " + deployment.getMetadata().getName() + " to " + (replicas - 1));
        }
    }

}
