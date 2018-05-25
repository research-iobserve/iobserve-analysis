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
package org.iobserve.analysis.data.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.iobserve.model.PCMModelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a model for privacy analysis purposes.
 *
 * @author Philipp Weimann
 *
 */
public class ModelGraph {
    protected static final Logger LOG = LoggerFactory.getLogger(ModelGraph.class);

    private final Set<DeploymentNode> servers;
    private final Set<ComponentNode> components;
    private final PCMModelHandler pcmModels;

    /**
     * Create a model graph.
     *
     * @param servers
     *            list of server nodes
     * @param components
     *            list of component nodes
     * @param pcmModels
     *            all PCM models TODO only use those models which are really necessary
     */
    public ModelGraph(final Collection<DeploymentNode> servers, final Collection<ComponentNode> components,
            final PCMModelHandler pcmModels) {
        this.servers = new HashSet<>(servers);
        this.components = new HashSet<>(components);
        this.pcmModels = pcmModels;
        // this.printGraph();
    }

    /**
     * Write graph to log file.
     */
    public void printGraph() {
        for (final DeploymentNode server : this.getServers()) {
            ModelGraph.LOG.info(server.toString());
        }
    }

    /**
     * @return the servers
     */
    public Set<DeploymentNode> getServers() {
        return this.servers;
    }

    /**
     * @return the components
     */
    public Set<ComponentNode> getComponents() {
        return this.components;
    }

    /**
     * @return the pcmModels
     */
    public PCMModelHandler getPcmModels() {
        return this.pcmModels;
    }

    /**
     * Compute hash code.
     *
     * @return returns the hash code
     */
    @Override
    public int hashCode() {
        return super.hashCode() + 1;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ModelGraph) {
            final ModelGraph compObj = (ModelGraph) obj;

            // Compare used servers
            boolean equalServers = true;
            for (final DeploymentNode server : this.servers) {
                if (server.getContainingComponents().size() == 0) {
                    continue;
                }

                final DeploymentNode compServer = compObj.servers.stream()
                        .filter(s -> s.getResourceContainerID().equals(server.getResourceContainerID())).findFirst()
                        .get();
                equalServers = server.equals(compServer);
                if (!equalServers) {
                    return false;
                }
            }

            // Compare components
            boolean equalComponents = true;
            for (final ComponentNode component : this.components) {
                final ComponentNode compComponent = compObj.components.stream()
                        .filter(s -> s.getAssemblyContextID().equals(component.getAssemblyContextID())).findFirst()
                        .get();
                equalComponents = component.equals(compComponent);
                if (!equalComponents) {
                    return false;
                }
            }
        }
        return true;
    }
}
