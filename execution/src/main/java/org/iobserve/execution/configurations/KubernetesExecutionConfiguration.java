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
import org.iobserve.execution.stages.ModelCollector;
import org.iobserve.execution.stages.kubernetes.AllocationExecutor;
import org.iobserve.execution.stages.kubernetes.DeallocationExecutor;
import org.iobserve.execution.stages.kubernetes.DeploymentExecutor;
import org.iobserve.execution.stages.kubernetes.UndeploymentExecutor;
import org.iobserve.stages.model.ModelFiles2ModelDirCollectorStage;
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

    /**
     * Creates a new configuration instance.
     *
     * @param executionPlanInputPort
     *            Input port for execution plan
     * @param runtimeModelInputPort
     *            Input port for runtime model
     * @param redeploymentModelInputPort
     *            Input port for redeployment model
     * @param workingDirectory
     *            Working directory of execution
     * @param runtimeModelDirectory
     *            Directory containing runtime models
     * @param redeploymentModelDirectory
     *            Directory containing redeployment models
     * @param correspondenceModelFile
     *            The correspondence model
     * @param imageLocator
     *            Locator prefix for image
     * @param subdomain
     *            Subdomain value of pods
     * @param namespace
     *            Kubernetes namespace
     */
    public KubernetesExecutionConfiguration(final int executionPlanInputPort, final int runtimeModelInputPort,
            final int redeploymentModelInputPort, final File workingDirectory, final File runtimeModelDirectory,
            final File redeploymentModelDirectory, final File correspondenceModelFile, final String imageLocator,
            final String subdomain, final String namespace) {

        final SingleConnectionTcpReaderStage executionPlanReader = new SingleConnectionTcpReaderStage(
                executionPlanInputPort, workingDirectory);
        final SingleConnectionTcpReaderStage runtimeModelReader = new SingleConnectionTcpReaderStage(
                runtimeModelInputPort, runtimeModelDirectory);
        final SingleConnectionTcpReaderStage redeploymentModelReader = new SingleConnectionTcpReaderStage(
                redeploymentModelInputPort, redeploymentModelDirectory);
        final ModelFiles2ModelDirCollectorStage runtimeModelCollector = new ModelFiles2ModelDirCollectorStage();
        final ModelFiles2ModelDirCollectorStage redeploymentModelCollector = new ModelFiles2ModelDirCollectorStage();
        final ModelCollector modelCollector = new ModelCollector();
        final ExecutionPlanDeserialization executionPlanDeserializaton = new ExecutionPlanDeserialization();
        final ExecutionPlan2AtomicActions executionPlan2AtomicActions = new ExecutionPlan2AtomicActions();

        final Map<String, Deployment> podsToDeploy = new HashMap<>();
        final DeploymentExecutor kubernetesDeploymentExecutor = new DeploymentExecutor(podsToDeploy,
                correspondenceModelFile, namespace);
        final UndeploymentExecutor kubernetesUndeploymentExecutor = new UndeploymentExecutor(namespace);
        final AllocationExecutor kubernetesAllocationExecutor = new AllocationExecutor(imageLocator, subdomain,
                podsToDeploy);
        final DeallocationExecutor kubernetesDeallocationExecutor = new DeallocationExecutor(namespace);
        final AtomicActionExecution atomicActionExecution = new AtomicActionExecution(kubernetesDeploymentExecutor,
                kubernetesUndeploymentExecutor, null, null, null, null, null, kubernetesAllocationExecutor,
                kubernetesDeallocationExecutor, null, null);

        this.connectPorts(runtimeModelReader.getOutputPort(), runtimeModelCollector.getInputPort());
        this.connectPorts(redeploymentModelReader.getOutputPort(), redeploymentModelCollector.getInputPort());
        this.connectPorts(executionPlanReader.getOutputPort(), modelCollector.getExecutionPlanInputPort());
        this.connectPorts(runtimeModelCollector.getOutputPort(), modelCollector.getRuntimeModelInputPort());
        this.connectPorts(redeploymentModelCollector.getOutputPort(), modelCollector.getRedeploymentModelInputPort());
        this.connectPorts(modelCollector.getOutputPort(), executionPlanDeserializaton.getInputPort());
        this.connectPorts(executionPlanDeserializaton.getOutputPort(), executionPlan2AtomicActions.getInputPort());
        this.connectPorts(executionPlan2AtomicActions.getOutputPort(), atomicActionExecution.getInputPort());

        modelCollector.declareActive();
    }
}
