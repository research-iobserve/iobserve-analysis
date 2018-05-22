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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.palladiosimulator.pcm.resourceenvironment.HDDProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
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
public class AllocationExecutor extends AbstractExecutor<AllocateNodeAction> {
    private static final String API_VERSION = "extensions/v1beta1";
    private static final String PROCESSINGRATE_LABEL_KEY = "processingRate";
    private static final String HDDREADRATE_LABEL_KEY = "hddReadProcessingRate";
    private static final String HDDWRITERATE_LABEL_KEY = "hddWriteProcessingRate";
    private static final String COMPONENT_LABEL_KEY = "component";
    private static final Logger LOGGER = LoggerFactory.getLogger(AllocationExecutor.class);

    private final String imageLocator;
    private final String subdomain;
    private final Map<String, Deployment> podsToDeploy;

    /**
     *
     * @param imageLocator
     *            Image location
     * @param subdomain
     *            Subdomain value
     * @param podsToDeploy
     *            Map containing blueprints for pods to deploy
     */
    public AllocationExecutor(final String imageLocator, final String subdomain,
            final Map<String, Deployment> podsToDeploy) {
        this.imageLocator = imageLocator;
        this.subdomain = subdomain;
        this.podsToDeploy = podsToDeploy;
    }

    @Override
    public void execute(final AllocateNodeAction action) {
        final ResourceContainer resourceContainer = action.getTargetResourceContainer();
        final String rcName = this.normalizeComponentName(resourceContainer.getEntityName());

        final Map<String, String> labels = this.computeDeploymentLabels(resourceContainer);

        // Build deployment blueprint
        final Deployment podDeployment = new DeploymentBuilder() //
                /**/ .withApiVersion(AllocationExecutor.API_VERSION) //
                /**/ .withKind("Deployment") //
                /**/ .withNewMetadata() //
                /*----*/ .withLabels(labels) //
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
                /*----------------   Image and name are inserted at deployment */
                /*----------------*/ .withImage(this.imageLocator) //
                /*----------------*/ .withName("") //
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

        if (AllocationExecutor.LOGGER.isDebugEnabled()) {
            AllocationExecutor.LOGGER
                    .debug("Created blueprint for pod deployment " + podDeployment.getMetadata().getName());
        }
    }

    /**
     * Computes deployment labels for a resource container.
     *
     * @param resourceContainer
     *            The resource container
     * @return A map containing the computed labels
     */
    private Map<String, String> computeDeploymentLabels(final ResourceContainer resourceContainer) {
        final EList<ProcessingResourceSpecification> specs = resourceContainer
                .getActiveResourceSpecifications_ResourceContainer();
        final EList<HDDProcessingResourceSpecification> hddSpecs = resourceContainer.getHddResourceSpecifications();
        final Map<String, String> labels = new HashMap<>();

        // Only first specification object and only processing rates used so far
        if (!specs.isEmpty()) {
            labels.put(AllocationExecutor.PROCESSINGRATE_LABEL_KEY,
                    specs.get(0).getProcessingRate_ProcessingResourceSpecification().getSpecification());
        }

        if (!hddSpecs.isEmpty()) {
            labels.put(AllocationExecutor.HDDREADRATE_LABEL_KEY,
                    hddSpecs.get(0).getReadProcessingRate().getSpecification());
            labels.put(AllocationExecutor.HDDWRITERATE_LABEL_KEY,
                    hddSpecs.get(0).getWriteProcessingRate().getSpecification());
        }

        return labels;
    }
}
