/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.adaptation.data;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iobserve.adaptation.data.graph.HostComponentAllocationGraph;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ComposedAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;

/**
 * This class provides all data required for planning and adapting the system.
 *
 * @author Philipp Weimann
 * @author Lars Blümke (terminology: (de-)allocate.sh -> (de-)replicate.sh, use of File instead of
 *         URI)
 */
public class AdaptationData {
    public static final String NODE_STARTUP_SCRIPT_NAME = "node_startup.sh";
    public static final String REPLICATE_SCRIPT_NAME = "replicate.sh";
    public static final String DEREPLICATE_SCRIPT_NAME = "dereplicate.sh";
    public static final String PRE_MIGRATE_SCRIPT_NAME = "pre_migrate.sh";
    public static final String POST_MIGRATE_SCRIPT_NAME = "post_migrate.sh";
    public static final String NODE_PRE_TERMINATE_SCRIPT_NAME = "node_pre_terminate.sh";

    private File runtimeModelDir;
    private HostComponentAllocationGraph runtimeGraph;

    private File reDeploymentModelDir;
    private HostComponentAllocationGraph reDeploymentGraph;

    private File deployablesDir;

    private List<AssemblyContextAction> acActions;
    private List<ResourceContainerAction> rcActions;

    private List<ComposedAction> executionOrder;

    private final Set<String> allocatedContexts = new HashSet<>();
    private final Set<String> deallocatedContexts = new HashSet<>();
    private final Set<String> migratedContexts = new HashSet<>();
    private final Set<String> terminatedGroups = new HashSet<>();

    /**
     * Data model for adaptation.
     */
    public AdaptationData() {
        // empty constructor
    }

    ////////////////////// GETTERS & SETTERS //////////////////////
    /**
     * @return the runtimeModelDir
     */
    public File getRuntimeModelDir() {
        return this.runtimeModelDir;
    }

    /**
     * @param runtimeModelDir
     *            the runtimeModelDir to set
     */
    public void setRuntimeModelDir(final File runtimeModelDir) {
        this.runtimeModelDir = runtimeModelDir;
    }

    /**
     * @return the runtimeGraph
     */
    public HostComponentAllocationGraph getRuntimeGraph() {
        return this.runtimeGraph;
    }

    /**
     * @param runtimeGraph
     *            the runtimeGraph to set
     */
    public void setRuntimeGraph(final HostComponentAllocationGraph runtimeGraph) {
        this.runtimeGraph = runtimeGraph;
    }

    /**
     * @return the reDeploymentModelDir
     */
    public File getReDeploymentModelDir() {
        return this.reDeploymentModelDir;
    }

    /**
     * @param reDeploymentModelDir
     *            the reDeploymentModelDir to set
     */
    public void setReDeploymentModelDir(final File reDeploymentModelDir) {
        this.reDeploymentModelDir = reDeploymentModelDir;
    }

    /**
     * @return the reDeploymentGraph
     */
    public HostComponentAllocationGraph getReDeploymentGraph() {
        return this.reDeploymentGraph;
    }

    /**
     * @param reDeploymentGraph
     *            the reDeploymentGraph to set
     */
    public void setReDeploymentGraph(final HostComponentAllocationGraph reDeploymentGraph) {
        this.reDeploymentGraph = reDeploymentGraph;
    }

    /**
     * @return the acActions
     */
    public List<AssemblyContextAction> getAcActions() {
        return this.acActions;
    }

    /**
     * @param acActions
     *            the acActions to set
     */
    public void setAcActions(final List<AssemblyContextAction> acActions) {
        this.acActions = acActions;
    }

    /**
     * @return the rcActions
     */
    public List<ResourceContainerAction> getRcActions() {
        return this.rcActions;
    }

    /**
     * @param rcActions
     *            the rcActions to set
     */
    public void setRcActions(final List<ResourceContainerAction> rcActions) {
        this.rcActions = rcActions;
    }

    /**
     * @return the executionOrder
     */
    public List<ComposedAction> getExecutionOrder() {
        return this.executionOrder;
    }

    /**
     * @param executionOrder
     *            the executionOrder to set
     */
    public void setExecutionOrder(final List<ComposedAction> executionOrder) {
        this.executionOrder = executionOrder;
    }

    public File getDeployablesDir() {
        return this.deployablesDir;
    }

    public void setDeployablesDir(final File deployablesDir) {
        this.deployablesDir = deployablesDir;
    }

    public Set<String> getAllocatedContexts() {
        return this.allocatedContexts;
    }

    public Set<String> getDeallocatedContexts() {
        return this.deallocatedContexts;
    }

    public Set<String> getMigratedContexts() {
        return this.migratedContexts;
    }

    public Set<String> getTerminatedGroups() {
        return this.terminatedGroups;
    }

}
