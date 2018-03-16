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
package org.iobserve.analysis.toggle;

/**
 * Constructs object that saves configurations settings for feature toggles. Can be passed as method
 * parameter. Set boolean parameters true, if feature is supposed to be enabled.
 *
 * @author Daniel Lucas
 *
 */
@Deprecated
public class FeatureToggle {
    private boolean deploymentToggle;
    private boolean undeploymentToggle;
    private boolean allocationToggle;
    private boolean deallocationToggle;
    private String behaviourToggle;
    private boolean dataFlowToggle;
    private boolean geolocationToggle;
    private boolean sinksToggle;

    /**
     * Creates FeatureToggle object. Expects boolean for toggles: deployment, undeployment,
     * allocation, deallocation, behaviour, dataflow, geolocation and sink. In that order. Set
     * parameter true, if feature is supposed to be enabled.
     *
     *
     */
    public FeatureToggle(final boolean deploymentToggle, final boolean undeploymentToggle,
            final boolean allocationToggle, final boolean deallocationToggle, final String behaviourToggle,
            final boolean dataflowToggle, final boolean geolocationToggle, final boolean sinksToggle) {
        this.deploymentToggle = deploymentToggle;
        this.undeploymentToggle = undeploymentToggle;
        this.allocationToggle = allocationToggle;
        this.deallocationToggle = deallocationToggle;
        this.behaviourToggle = behaviourToggle;
        this.dataFlowToggle = dataflowToggle;
        this.geolocationToggle = geolocationToggle;
        this.sinksToggle = sinksToggle;
    }

    // Deployment.
    public boolean isDeploymentToggle() {
        return this.deploymentToggle;
    }

    public void setDeploymentToggle(final boolean deploymentToggle) {
        this.deploymentToggle = deploymentToggle;
    }

    // Undeployment.
    public boolean isUndeploymentToggle() {
        return this.undeploymentToggle;
    }

    public void setUndeploymentToggle(final boolean undeploymentToggle) {
        this.undeploymentToggle = undeploymentToggle;
    }

    // Allocation.
    public boolean isAllocationToggle() {
        return this.allocationToggle;
    }

    public void setAllocationToggle(final boolean allocationToggle) {
        this.allocationToggle = allocationToggle;
    }

    public boolean isDeallocationToggle() {
        return this.deallocationToggle;
    }

    public void setDeallocationToggle(final boolean deallocationToggle) {
        this.deallocationToggle = deallocationToggle;
    }

    // Behaviour.
    public String getBehaviourToggle() {
        return this.behaviourToggle;
    }

    public void setBehaviourToggle(final String behaviourToggle) {
        this.behaviourToggle = behaviourToggle;
    }

    // Geolocation.
    public boolean isGeolocationToggle() {
        return this.geolocationToggle;
    }

    public void setGeolocationToggle(final boolean geolocationToggle) {
        this.geolocationToggle = geolocationToggle;
    }

    // Sink.
    public boolean isSinksToggle() {
        return this.sinksToggle;
    }

    public void setSinksToggle(final boolean sinksToggle) {
        this.sinksToggle = sinksToggle;
    }

    // Dataflow.
    public boolean isDataFlowToggle() {
        return this.dataFlowToggle;
    }

    public void setDataFlowToggle(final boolean dataFlowToggle) {
        this.dataFlowToggle = dataFlowToggle;
    }
}
