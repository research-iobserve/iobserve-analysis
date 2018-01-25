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
public class FeatureToggle {
    private boolean deploymentToggle;
    private boolean undeploymentToggle;
    private boolean allocationToggle;
    private boolean deallocationToggle;
    private boolean behaviourToggle;
    private boolean dataFlowToggle;
    private boolean geolocationToggle;
    private boolean SinksToggle;

    /**
     * Creates FeatureToggle object. Expects boolean for toggles: deployment, undeployment,
     * allocation, deallocation, behaviour, dataflow, geolocation and sink. In that order. Set
     * parameter true, if feature is supposed to be enabled.
     *
     * @param c
     * @param b
     *
     */
    public FeatureToggle(final boolean deploy, final boolean undeploy, final boolean alloc, final boolean dealloc,
            final boolean behav, final boolean datafl, final boolean geoloc, final boolean sinks) {
        this.deploymentToggle = deploy;
        this.setUndeploymentToggle(undeploy);
        this.allocationToggle = alloc;
        this.setDeallocationToggle(dealloc);
        this.behaviourToggle = behav;
        this.dataFlowToggle = datafl;
        this.geolocationToggle = geoloc;
        this.SinksToggle = sinks;
    }

    // Deployment.
    public boolean getDeploymentToggle() {
        return this.deploymentToggle;
    }

    public void setDeploymentToggle(final boolean deploymentToggle) {
        this.deploymentToggle = deploymentToggle;
    }

    // Undeployment.
    public boolean getUndeploymentToggle() {
        return this.undeploymentToggle;
    }

    public void setUndeploymentToggle(final boolean undeploymentToggle) {
        this.undeploymentToggle = undeploymentToggle;
    }

    // Allocation.
    public boolean getAllocationToggle() {
        return this.allocationToggle;
    }

    public void setAllocationToggle(final boolean allocationToggle) {
        this.allocationToggle = allocationToggle;
    }

    public boolean getDeallocationToggle() {
        return this.deallocationToggle;
    }

    public void setDeallocationToggle(final boolean deallocationToggle) {
        this.deallocationToggle = deallocationToggle;
    }

    // Behaviour.
    public boolean getBehaviourToggle() {
        return this.behaviourToggle;
    }

    public void setBehaviourToggle(final boolean behaviourToggle) {
        this.behaviourToggle = behaviourToggle;
    }

    // Geolocation.
    public boolean getGeolocationToggle() {
        return this.geolocationToggle;
    }

    public void setGeolocationToggle(final boolean geolocationToggle) {
        this.geolocationToggle = geolocationToggle;
    }

    // Sink.
    public boolean getSinksToggle() {
        return this.SinksToggle;
    }

    public void setSinksToggle(final boolean sinksToggle) {
        this.SinksToggle = sinksToggle;
    }

    // Dataflow.
    public boolean getDataFlowToggle() {
        return this.dataFlowToggle;
    }

    public void setDataFlowToggle(final boolean dataFlowToggle) {
        this.dataFlowToggle = dataFlowToggle;
    }
}
