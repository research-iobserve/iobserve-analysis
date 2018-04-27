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

import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.execution.stages.IExecutor;

import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Deployment via kubernetes.
 *
 * @author Lars Bluemke
 *
 */
public class KubernetesDeploymentExecutor extends AbstractKubernetesExecutor
        implements IExecutor<DeployComponentAction> {

    public KubernetesDeploymentExecutor(final String ip, final String port) {
        super(ip, port);
    }

    KubernetesClient kubernetes = this.getConnection();

    @Override
    public void execute(final DeployComponentAction deployComponentAction) {

        // Fabric8 community client library
        // KubernetesClient client = new DefaultKubernetesClient();
        // Pod pod = new Pod();
        // pod.
        // client.pods().create(item);
    }

}
