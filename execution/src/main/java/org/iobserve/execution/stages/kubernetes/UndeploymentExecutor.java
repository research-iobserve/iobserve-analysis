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

import io.fabric8.kubernetes.api.model.ReplicationController;
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

    public UndeploymentExecutor() {
    }

    @Override
    public void execute(final UndeployComponentAction action) {
        final KubernetesClient client = new DefaultKubernetesClient();
        final String podName = action.getTargetAllocationContext().getResourceContainer_AllocationContext()
                .getEntityName();
        final ReplicationController replicationController = client.replicationControllers().withName(podName).get();
        final int replicas = replicationController.getSpec().getReplicas();

        if (replicas > 1) {
            replicationController.getSpec().setReplicas(replicas - 1);
        }

        client.close();

        UndeploymentExecutor.LOGGER.info("Successfully deleted pod with name " + podName);
    }

}
