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
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * Deployment with kubernetes.
 *
 * @author Lars Bluemke
 *
 */
public class KubernetesDeploymentExecutor extends AbstractKubernetesExecutor
        implements IExecutor<DeployComponentAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KubernetesDeploymentExecutor.class);
    private final CorrespondenceModel correspondenceModel;

    public KubernetesDeploymentExecutor(final String ip, final String port,
            final CorrespondenceModel correspondenceModel, final String imagePrefix) {
        super(ip, port);
        this.correspondenceModel = correspondenceModel;
    }

    @Override
    public void execute(final DeployComponentAction action) {
        final KubernetesClient client = this.getConnection();

        this.correspondenceModel.getParts();

        final Pod pod = new PodBuilder() //
                /**/ .withApiVersion("v1") //
                /**/ .withKind("Pod") //
                /**/ .withNewMetadata() //
                /*----*/ .addToLabels("name", "order") //
                /*----*/ .withName("order") //
                /**/ .endMetadata() //
                /**/ .withNewSpec() //
                /*----*/ .withHostname("order") //
                /*----*/ .withSubdomain("jpetstore") //
                /*----*/ .addNewContainer() //
                /*--------*/ .withImage("blade1.se.internal:5000/jpetstore-order-service") //
                /*--------*/ .withName("order") //
                /*--------*/ .withNewResources() //
                /*--------*/ .endResources() //
                /*--------*/ .addNewEnv() //
                /*------------*/ .withName("LOGGER") //
                /*------------*/ .withValue("%LOGGER%") //
                /*--------*/ .endEnv() //
                /*----*/ .endContainer() //
                /**/ .endSpec() //
                /**/ .build(); //

        final Pod result = client.pods().create(pod);

        if (KubernetesDeploymentExecutor.LOGGER.isDebugEnabled()) {
            KubernetesDeploymentExecutor.LOGGER.debug("Created pod " + result.getMetadata().getName());
        }
    }

}
