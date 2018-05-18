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

import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.iobserve.execution.stages.IExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;

/**
 * Allocates a pod with kubernetes. Note that creating a pod with kubernetes immediately deploys an
 * instance of that pod. Thus, creating a pod resembles an allocation <i>and</i> a deployment.
 * Therefore we only create a pods specification here and leave the pod's actual creation to the
 * {@link DeploymentExecutor}.
 *
 * @author Lars Bluemke
 *
 */
public class AllocationExecutor implements IExecutor<AllocateNodeAction> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllocationExecutor.class);
    private static final String COMPONENT_LABEL_KEY = "component";

    private final String imageLocator;
    private final String subdomain;
    private final Map<String, Deployment> podsToDeploy;

    public AllocationExecutor(final String imageLocator, final String subdomain,
            final Map<String, Deployment> podsToDeploy) {
        this.imageLocator = imageLocator;
        this.subdomain = subdomain;
        this.podsToDeploy = podsToDeploy;
    }

    @Override
    public void execute(final AllocateNodeAction action) {
        AllocationExecutor.LOGGER.info("Executing allocation");

        final String rcName = action.getTargetResourceContainer().getEntityName().toLowerCase();

        AllocationExecutor.LOGGER.info("ID " + action.getTargetResourceContainer().getId());

        final Deployment podDeployment = new DeploymentBuilder() //
                /**/ .withApiVersion("extensions/v1beta1") //
                /**/ .withKind("Deployment") //
                /**/ .withNewMetadata() //
                /*----*/ .addToLabels("processingRate", "99") //
                /*----*/ .withName(rcName) //
                /**/ .endMetadata() //
                /**/ .withNewSpec() //
                /*----*/ .withReplicas(1) //
                /*----*/ .withNewSelector() //
                /*--------*/ .addToMatchLabels(AllocationExecutor.COMPONENT_LABEL_KEY, rcName) //
                /*----*/ .endSelector() //
                /*----*/ .withNewTemplate() //
                /*--------*/ .withNewMetadata() //
                /*------------*/ .addToLabels(AllocationExecutor.COMPONENT_LABEL_KEY, rcName) //
                /*------------*/ .withName(rcName) //
                /*--------*/ .endMetadata() //
                /*--------*/ .withNewSpec() //
                /*------------*/ .withHostname(rcName) //
                /*------------*/ .withSubdomain(this.subdomain) //
                /*------------*/ .addNewContainer() //
                /*----------------   Image name is inserted at deployment */
                /*----------------*/ .withImage(this.imageLocator) //
                /*----------------*/ .withName("order") //
                /*----------------*/ .withNewResources() //
                /*----------------*/ .endResources() //
                /*----------------*/ .addNewEnv() //
                /*--------------------*/ .withName("LOGGER") //
                /*--------------------*/ .withValue("%LOGGER%") //
                /*----------------*/ .endEnv() //
                // /*----------------*/ .addNewEnv() //
                // /*--------------------*/ .withName("JPETSTORE_DOMAIN") //
                // /*--------------------*/ .withValue(".jpetstore") //
                // /*----------------*/ .endEnv() //
                /*------------*/ .endContainer() //
                /*--------*/ .endSpec() //
                /*----*/ .endTemplate() //
                /**/ .endSpec() //
                .build();

        this.podsToDeploy.put(rcName, podDeployment);

        AllocationExecutor.LOGGER.info("Created blueprint for pod deployment " + podDeployment.getMetadata().getName());
    }

}
