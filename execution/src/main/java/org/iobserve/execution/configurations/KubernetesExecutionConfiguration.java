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
package org.iobserve.execution.configurations;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import teetime.framework.Configuration;

import org.iobserve.execution.stages.AtomicActionExecution;
import org.iobserve.execution.stages.ExecutionPlan2AtomicActions;
import org.iobserve.execution.stages.ExecutionPlanDeserialization;
import org.iobserve.execution.stages.kubernetes.AllocationExecutor;
import org.iobserve.execution.stages.kubernetes.DeallocationExecutor;
import org.iobserve.execution.stages.kubernetes.DeploymentExecutor;
import org.iobserve.execution.stages.kubernetes.UndeploymentExecutor;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.stages.source.SingleConnectionTcpReaderStage;

import io.fabric8.kubernetes.api.model.extensions.Deployment;

/**
 * Configuration for the stages of the execution service. This configuration uses the kubernetes
 * stages for the execution.
 *
 * @author Lars Bluemke
 *
 */
public class KubernetesExecutionConfiguration extends Configuration {

    public KubernetesExecutionConfiguration(final int executionPlanInputPort, final File executionPlanDirectory,
            final CorrespondenceModel correspondenceModel, final String imagePrefix, final String subdomain) {

        final SingleConnectionTcpReaderStage executionPlanReader = new SingleConnectionTcpReaderStage(
                executionPlanInputPort, executionPlanDirectory);
        final ExecutionPlanDeserialization executionPlanDeserializaton = new ExecutionPlanDeserialization();
        final ExecutionPlan2AtomicActions executionPlan2AtomicActions = new ExecutionPlan2AtomicActions();

        final Map<String, Deployment> podsToDeploy = new HashMap<>();
        final DeploymentExecutor kubernetesDeploymentExecutor = new DeploymentExecutor(podsToDeploy,
                correspondenceModel);
        final UndeploymentExecutor kubernetesUndeploymentExecutor = new UndeploymentExecutor();
        final AllocationExecutor kubernetesAllocationExecutor = new AllocationExecutor(imagePrefix, subdomain,
                podsToDeploy);
        final DeallocationExecutor kubernetesDeallocationExecutor = new DeallocationExecutor();
        final AtomicActionExecution atomicActionExecution = new AtomicActionExecution(kubernetesDeploymentExecutor,
                kubernetesUndeploymentExecutor, null, null, null, null, null, kubernetesAllocationExecutor,
                kubernetesDeallocationExecutor, null, null);

        this.connectPorts(executionPlanReader.getOutputPort(), executionPlanDeserializaton.getInputPort());
        this.connectPorts(executionPlanDeserializaton.getOutputPort(), executionPlan2AtomicActions.getInputPort());
        this.connectPorts(executionPlan2AtomicActions.getOutputPort(), atomicActionExecution.getInputPort());
    }
}
