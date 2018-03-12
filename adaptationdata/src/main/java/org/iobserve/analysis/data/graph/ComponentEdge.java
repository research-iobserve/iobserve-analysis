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

import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a model of a fully specified pcm assembly connector for the purpose of privacy
 * analysis.
 *
 * @author Philipp Weimann
 */
public class ComponentEdge {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentEdge.class);

    private final String id;
    private final String assemblyConnectorName;
    private final ComponentNode providingNode;
    private final ComponentNode requiringNode;
    private final DataPrivacyLvl privacyLvl;

    /**
     * The constructor for the edge.
     *
     * @param id
     *            the assembly connector privacy id
     * @param assemblyConnectorName
     *            the entity name
     * @param providingNode
     *            the edges providing component
     * @param requiringNode
     *            the edges requiring component
     * @param privacyLvl
     *            the edges DataPrivacyLvl
     */
    public ComponentEdge(final String id, final String assemblyConnectorName, final ComponentNode providingNode,
            final ComponentNode requiringNode, final DataPrivacyLvl privacyLvl) {
        this.id = id;
        this.assemblyConnectorName = assemblyConnectorName;
        this.providingNode = providingNode;
        this.requiringNode = requiringNode;
        this.privacyLvl = privacyLvl;
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return the providingNode
     */
    public ComponentNode getProvidingNode() {
        return this.providingNode;
    }

    /**
     * @return the requiringNode
     */
    public ComponentNode getRequiringNode() {
        return this.requiringNode;
    }

    /**
     * This method returns the according edge partner.
     *
     * @param firstNode
     *            a component at either end of the edge
     * @return the component not given as a firstNode, completing the edge partners. Returns null if
     *         firstNode is not a edge partner.
     */
    public ComponentNode getEdgePartner(final ComponentNode firstNode) {
        if (firstNode == this.providingNode) {
            return this.requiringNode;
        } else if (firstNode == this.requiringNode) {
            return this.providingNode;
        } else {
            ComponentEdge.LOGGER.error("ERROR: no edge partner found for node: " + firstNode.getAssemblyContextID());
        }
        return null;
    }

    /**
     * @return the privacyLvl
     */
    public DataPrivacyLvl getPrivacyLvl() {
        return this.privacyLvl;
    }

    /**
     * Compute hash code.
     *
     * @return returns the hash code
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ComponentEdge) {
            final ComponentEdge compObj = (ComponentEdge) obj;
            if (this.id.equals(compObj.id) && this.assemblyConnectorName.equals(compObj.assemblyConnectorName)
                    && this.privacyLvl == compObj.privacyLvl
                    && this.providingNode.getAssemblyContextID()
                            .contentEquals(compObj.providingNode.getAssemblyContextID())
                    && this.requiringNode.getAssemblyContextID()
                            .contentEquals(compObj.requiringNode.getAssemblyContextID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the assemblyConnectorName
     */
    public String getAssemblyConnectorName() {
        return this.assemblyConnectorName;
    }
}
