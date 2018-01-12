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

import java.util.HashSet;
import java.util.Set;

import com.neovisionaries.i18n.CountryCode;

/**
 * This class is a model of a fully specified pcm resource container for the purpose of privacy
 * analysis.
 *
 * @author Philipp Weimann
 */
public class DeploymentNode {

    private final String resourceContainerID;
    private final String resourceContainerName;
    private final int isoCountryCode;

    private final Set<ComponentNode> containingComponents;

    /**
     * The constructor.
     *
     * @param resourceContainerID
     *            the id of the represented resource container
     * @param resourceContainerName
     *            name of the resource container
     * @param isoCountryCode
     *            the (iso) country code of the country the resource container is located in
     */
    public DeploymentNode(final String resourceContainerID, final String resourceContainerName,
            final int isoCountryCode) {
        this.resourceContainerID = resourceContainerID;
        this.resourceContainerName = resourceContainerName;
        this.isoCountryCode = isoCountryCode;

        this.containingComponents = new HashSet<>();
    }

    /**
     * Adds a component to the hosted/deployed components.
     *
     * @param componentNode
     *            the hosted component
     * @return whether the adding was successful
     */
    public boolean addComponent(final ComponentNode componentNode) {
        return this.containingComponents.add(componentNode);
    }

    /*
     * GETTERS
     */
    /**
     * @return the Iso Country Code
     */
    public int getIsoCountryCode() {
        return this.isoCountryCode;
    }

    /**
     * @return the Iso Alpha3 Country Code
     */
    public String getIso3CountryCode() {
        try {
            return CountryCode.getByCode(this.getIsoCountryCode()).getAlpha3();
        } catch (final NullPointerException e) {
            return "ERROR";
        }
    }

    /**
     * @return resource container id
     */
    public String getResourceContainerID() {
        return this.resourceContainerID;
    }

    /**
     * @return the resourceContainerName
     */
    public String getResourceContainerName() {
        return this.resourceContainerName;
    }

    /**
     * @return the containingComponents
     */
    public Set<ComponentNode> getContainingComponents() {
        return this.containingComponents;
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
        if (obj instanceof DeploymentNode) {
            final DeploymentNode compObj = (DeploymentNode) obj;
            if (this.resourceContainerID.equals(compObj.resourceContainerID)
                    && this.resourceContainerName.equals(compObj.resourceContainerName)
                    && this.isoCountryCode == compObj.isoCountryCode) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Server: " + this.resourceContainerID);
        sb.append("\t-- Location: " + this.isoCountryCode);
        sb.append(" (" + this.getIso3CountryCode() + ")");
        sb.append("\t-- Name: " + this.getResourceContainerName() + "\n");

        sb.append("-Comp:\t ID \t\t\tCompPrivayLvl \tPers \tDeP \tAnonym \tComponent Name\n");

        for (final ComponentNode component : this.containingComponents) {
            sb.append("\t" + component.toString());
        }

        return sb.toString();
    }

}
