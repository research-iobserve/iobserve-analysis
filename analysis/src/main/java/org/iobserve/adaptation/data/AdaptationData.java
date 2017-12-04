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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;

/**
 * This class provides all data required for planning and adapting the system.
 *
 * @author Philipp Weimann
 */
public class AdaptationData {
    public static final String NODE_STARTUP_SCRIPT_NAME = "node_startup.sh";
    public static final String ALLOCATE_SCRIPT_NAME = "allocate.sh";
    public static final String DEALLOCATE_SCRIPT_NAME = "deallocate.sh";
    public static final String PRE_MIGRATE_SCRIPT_NAME = "pre_migrate.sh";
    public static final String POST_MIGRATE_SCRIPT_NAME = "post_migrate.sh";
    public static final String NODE_PRE_TERMINATE_SCRIPT_NAME = "node_pre_terminate.sh";

    private URI runtimeModelURI;
    private ModelGraph runtimeGraph;

    private URI reDeploymentURI;
    private ModelGraph reDeploymentGraph;

    private URI deployablesFolderURI;

    private List<AssemblyContextAction> acActions;
    private List<ResourceContainerAction> rcActions;

    private List<Action> executionOrder;

    private final Set<String> allocatedContexts = new HashSet<>();
    private final Set<String> deallocatedContexts = new HashSet<>();
    private final Set<String> migratedContexts = new HashSet<>();
    private final Set<String> terminatedGroups = new HashSet<>();

    ////////////////////// GETTERS & SETTERS //////////////////////
    /**
     * @return the runtimeModelURI
     */
    public URI getRuntimeModelURI() {
        return this.runtimeModelURI;
    }

    /**
     * @param runtimeModelURI
     *            the runtimeModelURI to set
     */
    public void setRuntimeModelURI(final URI runtimeModelURI) {
        this.runtimeModelURI = runtimeModelURI;
    }

    /**
     * @return the runtimeGraph
     */
    public ModelGraph getRuntimeGraph() {
        return this.runtimeGraph;
    }

    /**
     * @param runtimeGraph
     *            the runtimeGraph to set
     */
    public void setRuntimeGraph(final ModelGraph runtimeGraph) {
        this.runtimeGraph = runtimeGraph;
    }

    /**
     * @return the reDeploymentURI
     */
    public URI getReDeploymentURI() {
        return this.reDeploymentURI;
    }

    /**
     * @param reDeploymentURI
     *            the reDeploymentURI to set
     */
    public void setReDeploymentURI(final URI reDeploymentURI) {
        this.reDeploymentURI = reDeploymentURI;
    }

    /**
     * @return the reDeploymentGraph
     */
    public ModelGraph getReDeploymentGraph() {
        return this.reDeploymentGraph;
    }

    /**
     * @param reDeploymentGraph
     *            the reDeploymentGraph to set
     */
    public void setReDeploymentGraph(final ModelGraph reDeploymentGraph) {
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
    public List<Action> getExecutionOrder() {
        return this.executionOrder;
    }

    /**
     * @param executionOrder
     *            the executionOrder to set
     */
    public void setExecutionOrder(final List<Action> executionOrder) {
        this.executionOrder = executionOrder;
    }

    public URI getDeployablesFolderURI() {
        return this.deployablesFolderURI;
    }

    public void setDeployablesFolderURI(final URI deployablesFolderURI) {
        this.deployablesFolderURI = deployablesFolderURI;
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
