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

import org.iobserve.adaptation.executionplan.DeallocateNodeAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Deallocates a pod with kubernetes by deleting the pod.
 *
 * @author Lars Bluemke
 *
 */
public class DeallocationExecutor extends AbstractExecutor<DeallocateNodeAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeallocationExecutor.class);

    private final String namespace;

    /**
     * Creates a new deallocation executor.
     *
     * @param namespace
     *            Kubernetes namespace
     */
    public DeallocationExecutor(final String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void execute(final DeallocateNodeAction action) {
        final KubernetesClient client = new DefaultKubernetesClient();
        final String rcName = this.normalizeComponentName(action.getTargetResourceContainer().getEntityName());

        client.extensions().deployments().inNamespace(this.namespace).withName(rcName).delete();
        client.close();

        if (DeallocationExecutor.LOGGER.isDebugEnabled()) {
            DeallocationExecutor.LOGGER.debug("Successfully deleted pod deployment with name " + rcName);
        }
    }

}
