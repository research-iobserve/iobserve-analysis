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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
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

    private final Map<String, Pod> podsToBuild;

    public DeploymentExecutor(final Map<String, Pod> podsToBuild) {
        this.podsToBuild = podsToBuild;
    }

    @Override
    public void execute(final DeployComponentAction action) {
        DeploymentExecutor.LOGGER.info("Trying to get connection to kubernetes");

        final KubernetesClient client = new DefaultKubernetesClient();
        final ReplicationController replicationController = client.replicationControllers().withName("fancypodname")
                .get();

        if (replicationController != null) {
            // Increase number of replicas if pod is already running...
            final int replicas = replicationController.getSpec().getReplicas();
            replicationController.getSpec().setReplicas(replicas + 1);
        } else {
            // ... create new pod if this is not the case
            final Pod pod = this.podsToBuild.get("fancypodname");
            final Pod result = client.pods().create(pod);

            DeploymentExecutor.LOGGER.info("Created pod " + result.getMetadata().getName());
        }

        client.close();
    }

}
