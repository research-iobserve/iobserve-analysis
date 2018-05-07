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

import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Undeployment via kubernetes.
 *
 * @author Lars Bluemke
 *
 */
public class KubernetesUndeploymentExecutor extends AbstractKubernetesExecutor
        implements IExecutor<UndeployComponentAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesUndeploymentExecutor.class);

    public KubernetesUndeploymentExecutor(final String ip, final String port) {
        super(ip, port);
    }

    @Override
    public void execute(final UndeployComponentAction action) {
        final KubernetesClient client = this.getConnection();
        final String namespace = client.getNamespace();
        final String podName = action.getTargetAllocationContext().getAssemblyContext_AllocationContext()
                .getEntityName(); // entity name = pod name?

        client.pods().inNamespace(namespace).withName(podName).delete();

        KubernetesUndeploymentExecutor.LOGGER.debug("Successfully deleted pod with name " + podName);
    }

}
