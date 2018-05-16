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
import org.iobserve.model.correspondence.CorrespondenceModel;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;

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
    private final String imageLocator;
    private final CorrespondenceModel correspondenceModel;
    private final Map<String, Pod> podsToBuild;

    public AllocationExecutor(final String imageLocator, final CorrespondenceModel correspondenceModel,
            final Map<String, Pod> podsToBuild) {
        this.imageLocator = imageLocator;
        this.correspondenceModel = correspondenceModel;
        this.podsToBuild = podsToBuild;
    }

    @Override
    public void execute(final AllocateNodeAction action) {

        final String podname = action.getTargetResourceContainer().getEntityName();
        final String imageName = "get this from correspondence model";

        final Pod pod = new PodBuilder() //
                /**/ .withApiVersion("v1") //
                /**/ .withKind("Pod") //
                /**/ .withNewMetadata() //
                /*----*/ .addToLabels("name", podname) //
                /*----*/ .withName(podname) //
                /**/ .endMetadata() //
                /**/ .withNewSpec() //
                /*----*/ .withHostname(podname) //
                /*----*/ .withSubdomain("jpetstore") //
                /*----*/ .addNewContainer() //
                /*--------*/ .withImage(this.imageLocator + "/" + imageName) //
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

        this.podsToBuild.put(podname, pod); // Can I assume that podnames are unique?
    }

}
