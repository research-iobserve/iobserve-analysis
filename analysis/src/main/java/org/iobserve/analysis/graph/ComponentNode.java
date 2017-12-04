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
package org.iobserve.analysis.graph;

import java.util.LinkedHashSet;
import java.util.Set;

import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

/**
 * This class is a model of a fully specified pcm component for the purpose of privacy analysis.
 *
 * @author Philipp Weimann
 */
public class ComponentNode {

    private final String assemblyContextID;
    private final String assemblyName;
    private final String repositoryComponentID;
    private final String allocationContextID;
    private DataPrivacyLvl privacyLvl;
    private final DeploymentNode hostServer;
    private final Set<ComponentEdge> edges;

    /**
     * The constructor
     * 
     * @param assemblyContextID
     *            the pcm id of the represented (composed) component
     * @param privacyLvl
     *            the privacy level of the represended component
     * @param hostContext
     *            the model representation of the resource container the component is deployed on
     */
    public ComponentNode(final String assemblyContextID, final String assemblyName, final DataPrivacyLvl privacyLvl,
            final DeploymentNode hostContext, final String repositoryComponentID, final String allocationContextID) {
        this.assemblyContextID = assemblyContextID;
        this.assemblyName = assemblyName;
        this.repositoryComponentID = repositoryComponentID;
        this.allocationContextID = allocationContextID;
        this.privacyLvl = privacyLvl;
        this.hostServer = hostContext;
        this.edges = new LinkedHashSet<>();
    }

    /**
     * Add a component node, this component is communicating with.
     * 
     * @param communicationEge
     *            the communication partner
     * @return whether the add was successful
     */
    public boolean addCommunicationEdge(final ComponentEdge communicationEdge) {

        return this.edges.add(communicationEdge);
    }

    /*
     * GETTERS
     */
    /**
     * @return the assembly context id
     */
    public String getAssemblyContextID() {
        return this.assemblyContextID;
    }

    /**
     * @return the assemblyName
     */
    public String getAssemblyName() {
        return this.assemblyName;
    }

    /**
     * @return the repositoryComponentID
     */
    public String getRepositoryComponentID() {
        return this.repositoryComponentID;
    }

    /**
     * @return the allocationContextID
     */
    public String getAllocationContextID() {
        return this.allocationContextID;
    }

    /**
     * @return the data privacy level
     */
    public DataPrivacyLvl getPrivacyLvl() {
        return this.privacyLvl;
    }

    /**
     * @param privacyLvl
     *            the privacyLvl to set
     */
    public void setPrivacyLvl(final DataPrivacyLvl privacyLvl) {
        this.privacyLvl = privacyLvl;
    }

    /**
     * @return the DeplyomentNode, the component is deployed on
     */
    public DeploymentNode getHostServer() {
        return this.hostServer;
    }

    /**
     * @return the Edges {@link ComponentEdge} of the component
     */
    public ComponentEdge[] getEdges() {
        return this.edges.toArray(new ComponentEdge[this.edges.size()]);
    }

    /**
     * Checks whether the given object and the current ComponentNode are equal. Some properties
     * don't get compared since they can differ based on previously performed operations on the
     * graph.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ComponentNode) {
            final ComponentNode compObj = (ComponentNode) obj;
            if (this.assemblyContextID.equals(compObj.assemblyContextID)
                    && this.assemblyName.equals(compObj.assemblyName)
                    && this.repositoryComponentID.equals(compObj.repositoryComponentID)
                    && this.allocationContextID.equals(compObj.allocationContextID)
                    && this.hostServer.equals(compObj.hostServer)) {

                return true;
            }
        }
        return false;

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getAssemblyContextID() + "\t" + this.privacyLvl.toString() + "\t");

        sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.PERSONAL).count() + "\t");
        sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.DEPERSONALIZED).count() + "\t");
        sb.append(this.edges.stream().filter((s) -> s.getPrivacyLvl() == DataPrivacyLvl.ANONYMIZED).count() + "\t");

        sb.append(this.getAssemblyName() + "\n");
        return sb.toString();
    }

}
