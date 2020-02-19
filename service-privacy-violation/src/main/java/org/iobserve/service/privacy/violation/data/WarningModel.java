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
package org.iobserve.service.privacy.violation.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iobserve.analysis.deployment.data.IPCMDeploymentEvent;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.IWarningEdge;
import org.iobserve.stages.data.IErrorMessages;

/**
 * Collection of warnings.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class WarningModel implements IErrorMessages, IWarningEdge {
    private IPCMDeploymentEvent event;
    private final List<String> messages = new ArrayList<>();
    private List<Edge> warningEdges = new ArrayList<>();
    private Date date;

    /**
     * Create a warnings result object.
     */
    public WarningModel() {
        // empty data class constructor
    }

    @Override
    public final List<String> getMessages() {
        return this.messages;
    }

    @Override
    public void addMessage(final String message) {
        this.messages.add(message);
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public void setDate(final Date date) {
        this.date = date;
    }

    @Override
    public List<Edge> getWarningEdges() {
        return this.warningEdges;
    }

    @Override
    public void setWarningEdges(final List<Edge> edges) {
        this.warningEdges = edges;

    }

    @Override
    public void addWarningEdge(final Edge edge) {
        this.warningEdges.add(edge);

    }

    public IPCMDeploymentEvent getEvent() {
        return this.event;
    }

    public void setEvent(final IPCMDeploymentEvent event) {
        this.event = event;
    }

}
