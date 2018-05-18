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
import org.iobserve.execution.stages.IExecutor;
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
public class DeallocationExecutor implements IExecutor<DeallocateNodeAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeallocationExecutor.class);

    @Override
    public void execute(final DeallocateNodeAction action) {
        DeallocationExecutor.LOGGER.info("Executing deallocation");

        final KubernetesClient client = new DefaultKubernetesClient();
        final String rcName = action.getTargetResourceContainer().getEntityName().toLowerCase();

        client.extensions().deployments().inNamespace("default").withName(rcName).delete();
        client.close();

        DeallocationExecutor.LOGGER.info("Successfully deleted pod deployment with name " + rcName);
    }

}
