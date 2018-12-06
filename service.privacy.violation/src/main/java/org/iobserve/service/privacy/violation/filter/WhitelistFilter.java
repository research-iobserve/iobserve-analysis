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
package org.iobserve.service.privacy.violation.filter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.AnalysisExperimentLogging;
import org.iobserve.common.record.EventTypes;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Receives the {@link ProbeManagementData} and fills in a whitelist before it is send to the output
 * port.
 *
 * @author Marc Adolf
 *
 */
public class WhitelistFilter extends AbstractConsumerStage<ProbeManagementData> {
    private final ModelResource<Allocation> allocationResource;
    private final ModelResource<ResourceEnvironment> resourceEnvironmentResource;

    private final OutputPort<ProbeManagementData> outputPort = this.createOutputPort();

    public WhitelistFilter(final ModelResource<Allocation> allocationResource,
            final ModelResource<ResourceEnvironment> resourceEnvironmentResource) {
        this.allocationResource = allocationResource;
        this.resourceEnvironmentResource = resourceEnvironmentResource;

    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#execute()
     */
    @Override
    protected void execute(final ProbeManagementData element) throws Exception {
        AnalysisExperimentLogging.logEvent(0, EventTypes.NONE, "white-list-filter");
        final List<String> whitelist = this.computeWhitelist(element.getWarnedMethods());
        element.setWhitelist(whitelist);
        this.outputPort.send(element);
    }

    private List<String> computeWhitelist(final Map<AllocationContext, Set<OperationSignature>> warnedMethods)
            throws DBException {
        final Set<String> blacklist = this.computeForbiddenIps(warnedMethods);
        final Set<String> allIps = this.computeAvailableIps();
        final Set<String> whitelist = new HashSet<>(allIps);
        whitelist.removeAll(blacklist);

        this.logger.debug("All available IPs: {}", allIps);
        this.logger.debug("Forbidden IPs: {}", blacklist);
        this.logger.debug("Computed whitelist: {}", whitelist);

        return new LinkedList<>(whitelist);
    }

    private Set<String> computeAvailableIps() throws DBException {
        final List<AllocationContext> allocations = this.allocationResource
                .collectAllObjectsByType(AllocationContext.class, AllocationPackage.Literals.ALLOCATION_CONTEXT);
        final Set<String> availableIps = new LinkedHashSet<>();

        for (final AllocationContext allocation : allocations) {
            try {
                availableIps.add(this.resourceEnvironmentResource
                        .resolve(allocation.getResourceContainer_AllocationContext()).getEntityName());
            } catch (InvocationException | DBException e) {
                this.logger.error(
                        "Could not resolve resource container during the computation of the IP from: " + allocation, e);
            }
        }

        return availableIps;
    }

    private Set<String> computeForbiddenIps(final Map<AllocationContext, Set<OperationSignature>> warnedMethods) {
        final Set<AllocationContext> allocationSet = new LinkedHashSet<>();
        allocationSet.addAll(warnedMethods.keySet());
        final Set<String> blacklistSet = new HashSet<>();

        for (final AllocationContext allocation : allocationSet) {
            try {
                blacklistSet.add(this.resourceEnvironmentResource
                        .resolve(allocation.getResourceContainer_AllocationContext()).getEntityName());
            } catch (InvocationException | DBException e) {
                this.logger.error(
                        "Could not resolve resource container during the computation of the IP of the black list from: "
                                + allocation,
                        e);
            }
        }

        return blacklistSet;
    }

    public OutputPort<ProbeManagementData> getOutputPort() {
        return this.outputPort;
    }

}
