/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.splitter;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.misc.KiekerMetadataRecord;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * The record switch filter is used to scan the event stream and send events based on their type to
 * different output ports.
 *
 * @author Reiner Jung
 *
 */
public class Splitter extends AbstractConsumerStage<IMonitoringRecord> {

    private static final Logger LOGGER = LogManager.getLogger(Splitter.class);

    private final OutputPort<IMonitoringRecord> adapterOutputPort = this.createOutputPort();
    private final OutputPort<IMonitoringRecord> enterpriseOutputPort = this.createOutputPort();
    private final OutputPort<IMonitoringRecord> storeOutputPort = this.createOutputPort();
    private final OutputPort<IMonitoringRecord> registryOutputPort = this.createOutputPort();
    private final OutputPort<IMonitoringRecord> webFrontendMetaPort = this.createOutputPort();

    private final Map<Long, TraceMetadata> traceRegisterMap = new HashMap<>();

    /** Statistics. */
    private int recordCount;

    private final String adapter;

    private final String enterprise;

    private final String store;

    private final String registry;

    private final String webFrontend;

    /**
     * Empty default constructor.
     */
    public Splitter(final String adapter, final String enterprise, final String store, final String registry,
            final String webFrontend) {
        this.adapter = adapter;
        this.enterprise = enterprise;
        this.store = store;
        this.registry = registry;
        this.webFrontend = webFrontend;
    }

    @Override
    protected void execute(final IMonitoringRecord element) {
        this.recordCount++;
        if (element instanceof TraceMetadata) {
            final TraceMetadata traceMetadata = (TraceMetadata) element;
            this.traceRegisterMap.put(traceMetadata.getTraceId(), traceMetadata);
            if (this.adapter.equals(traceMetadata.getHostname())) {
                this.getAdapterOutputPort().send(element);
            } else if (this.enterprise.equals(traceMetadata.getHostname())) {
                this.getEnterpriseOutputPort().send(element);
            } else if (this.store.equals(traceMetadata.getHostname())) {
                this.getStoreOutputPort().send(element);
            } else if (this.registry.equals(traceMetadata.getHostname())) {
                this.getRegistryOutputPort().send(element);
            } else if (this.webFrontend.equals(traceMetadata.getHostname())) {
                this.getWebFrontendOutputPort().send(element);
            } else {
                this.getAdapterOutputPort().send(element);
                this.getEnterpriseOutputPort().send(element);
                this.getStoreOutputPort().send(element);
                this.getWebFrontendOutputPort().send(element);
            }
        } else if (element instanceof ITraceRecord) {
            final TraceMetadata metadata = this.traceRegisterMap.get(((ITraceRecord) element).getTraceId());
            if (this.adapter.equals(metadata.getHostname())) {
                this.getAdapterOutputPort().send(element);
            } else if (this.enterprise.equals(metadata.getHostname())) {
                this.getEnterpriseOutputPort().send(element);
            } else if (this.store.equals(metadata.getHostname())) {
                this.getStoreOutputPort().send(element);
            } else if (this.registry.equals(metadata.getHostname())) {
                this.getRegistryOutputPort().send(element);
            } else if (this.webFrontend.equals(metadata.getHostname())) {
                this.getWebFrontendOutputPort().send(element);
            } else {
                this.getAdapterOutputPort().send(element);
                this.getEnterpriseOutputPort().send(element);
                this.getStoreOutputPort().send(element);
                this.getWebFrontendOutputPort().send(element);
            }
        } else if (element instanceof KiekerMetadataRecord) {
            /** ignore. */
            System.out.println("Metadata record " + element);
        } else {
            this.getAdapterOutputPort().send(element);
            this.getEnterpriseOutputPort().send(element);
            this.getStoreOutputPort().send(element);
            this.getWebFrontendOutputPort().send(element);
        }
    }

    /**
     * @return the deploymentOutputPort
     */
    public final OutputPort<IMonitoringRecord> getAdapterOutputPort() {
        return this.adapterOutputPort;
    }

    /**
     * @return the undeploymentOutputPort
     */
    public final OutputPort<IMonitoringRecord> getEnterpriseOutputPort() {
        return this.enterpriseOutputPort;
    }

    /**
     * @return the flowOutputPort
     */
    public final OutputPort<IMonitoringRecord> getStoreOutputPort() {
        return this.storeOutputPort;
    }

    /**
     * @return the flowOutputPort
     */
    public final OutputPort<IMonitoringRecord> getRegistryOutputPort() {
        return this.registryOutputPort;
    }

    /**
     *
     * @return traceOutputPort
     */
    public OutputPort<IMonitoringRecord> getWebFrontendOutputPort() {
        return this.webFrontendMetaPort;
    }

    public long getRecordCount() {
        return this.recordCount;
    }
}
