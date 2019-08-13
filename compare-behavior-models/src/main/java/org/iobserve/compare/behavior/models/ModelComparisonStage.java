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
package org.iobserve.compare.behavior.models;

import java.util.Arrays;
import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.service.behavior.analysis.clustering.GraphEditDistance;
import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.service.behavior.analysis.model.EventGroup;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compare two behavior models and compute their differences.
 *
 * @author Lars Jürgensen
 *
 */
public class ModelComparisonStage extends AbstractStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelComparisonStage.class);

    private final InputPort<BehaviorModelGED> referenceModelInputPort = this.createInputPort();
    private final InputPort<BehaviorModelGED> testModelInputPort = this.createInputPort();
    private final OutputPort<BehaviorModelDifference> resultPort = this.createOutputPort();

    private BehaviorModelGED referenceModel;

    private BehaviorModelGED testModel;

    /**
     * Model comparison stage.
     */
    public ModelComparisonStage() {
        // empty
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#execute()
     */
    @Override
    protected void execute() throws Exception {
        if (this.referenceModel == null) {
            this.referenceModel = this.referenceModelInputPort.receive();
        }
        if (this.testModel == null) {
            this.testModel = this.testModelInputPort.receive();
        }

        if ((this.referenceModel != null) && (this.testModel != null)) {

            ModelComparisonStage.LOGGER.debug("received a reference and a test model");
            final BehaviorModelDifference result = new BehaviorModelDifference();
            result.setGraphEditDistance(new GraphEditDistance().calculate(this.testModel, this.referenceModel));

            this.findAdditionalNodes(this.referenceModel, this.testModel, result.getReferenceNodes());
            this.findAdditionalNodes(this.testModel, this.referenceModel, result.getTestModelNodes());

            this.findAdditionalEdges(this.referenceModel, this.testModel, result.getReferenceEdges());
            this.findAdditionalEdges(this.testModel, this.referenceModel, result.getTestModelEdges());

            this.findAdditionalEventGroups(this.referenceModel, this.testModel, result.getReferenceEventGroup());
            this.findAdditionalEventGroups(this.testModel, this.referenceModel, result.getTestModelEventGroup());

            this.findAdditionalEvents(this.referenceModel, this.testModel, result.getReferenceEvents());
            this.findAdditionalEvents(this.testModel, this.referenceModel, result.getTestModelEvents());

            this.getOutputPort().send(result);

            // NOPMD necessary to forget data
            this.referenceModel = null;
            this.testModel = null;
            ModelComparisonStage.LOGGER.debug("generated the differences between models");

        }

    }

    /**
     * If there is a node in modelA, but not in modelB, the node is added to the additionalNodes
     * list.
     *
     * @param modelA
     * @param modelB
     * @param additionalNodes
     */
    private void findAdditionalNodes(final BehaviorModelGED modelA, final BehaviorModelGED modelB,
            final List<BehaviorModelNode> additionalNodes) {

        for (final BehaviorModelNode node : modelA.getNodes().values()) {
            final BehaviorModelNode potentialMatch = modelB.getNodes().get(node.getName());

            if (potentialMatch == null) {
                additionalNodes.add(node);
            }
        }
    }

    /**
     * If there is an edge in modelA, but not in modelB, the edge is added to the additionalEdges
     * list.
     *
     * @param modelA
     * @param modelB
     * @param additionalEdges
     */
    private void findAdditionalEdges(final BehaviorModelGED modelA, final BehaviorModelGED modelB,
            final List<BehaviorModelEdge> additionalEdges) {

        for (final BehaviorModelEdge edge : modelA.getEdges()) {
            if (this.findMatchingEdge(edge, modelB) == null) {
                additionalEdges.add(edge);
            }

        }

    }

    /**
     * If there is an event group in modelA, but not in modelB, the event group is added to the
     * additionalEventGroups list.
     *
     * @param modelA
     * @param modelB
     * @param additionalEventGroups
     */
    private void findAdditionalEventGroups(final BehaviorModelGED modelA, final BehaviorModelGED modelB,
            final List<EventGroup> additionalEventGroups) {
        for (final BehaviorModelEdge edge : modelA.getEdges()) {
            for (final EventGroup eventGroup : edge.getEventGroups()) {
                final EventGroup match = this.findMatchingEventGroup(eventGroup, edge, modelB);
                if (match == null) {
                    additionalEventGroups.add(eventGroup);
                }
            }
        }
    }

    /**
     * If there is an event in modelA, but not in modelB, the event is added to the additionalEvents
     * list.
     *
     * @param modelA
     * @param modelB
     * @param additionalEvents
     */
    private void findAdditionalEvents(final BehaviorModelGED modelA, final BehaviorModelGED modelB,
            final List<PayloadAwareEntryCallEvent> additionalEvents) {
        for (final BehaviorModelEdge edge : modelA.getEdges()) {
            for (final EventGroup eventGroup : edge.getEventGroups()) {
                final EventGroup matchingGroup = this.findMatchingEventGroup(eventGroup, edge, modelB);
                if (matchingGroup == null) {
                    additionalEvents.addAll(eventGroup.getEvents());
                } else {
                    for (final PayloadAwareEntryCallEvent event1 : eventGroup.getEvents()) {
                        boolean matchFound = false;
                        for (final PayloadAwareEntryCallEvent event2 : matchingGroup.getEvents()) {
                            ModelComparisonStage.haveSameValues(event1, event2);
                            matchFound = true;
                            break;
                        }
                        // no matching event
                        if (!matchFound) {
                            additionalEvents.add(event1);
                        }
                    }
                }
            }
        }
    }

    /**
     * searches an edge in the model, with the same source and target nodes as the edge parameter.
     * The nodes are considered equal, if the names are equal
     *
     * @param edge
     * @param model
     * @return null, if no edge found and the edge otherwise
     */
    private BehaviorModelEdge findMatchingEdge(final BehaviorModelEdge edge, final BehaviorModelGED model) {
        final String sourceName = edge.getSource().getName();
        final String targetName = edge.getTarget().getName();

        final BehaviorModelNode sourceNode = model.getNodes().get(sourceName);
        final BehaviorModelNode targetNode = model.getNodes().get(targetName);

        if ((sourceNode == null) || (targetNode == null)) {
            return null;
        }

        return sourceNode.getOutgoingEdges().get(targetNode);
    }

    private EventGroup findMatchingEventGroup(final EventGroup eventGroup, final BehaviorModelEdge parentEdge,
            final BehaviorModelGED model) {
        final BehaviorModelEdge matchingEdge = this.findMatchingEdge(parentEdge, model);
        if (matchingEdge == null) {
            return null;
        }
        for (final EventGroup matchingEventGroup : matchingEdge.getEventGroups()) {
            if (eventGroup.hasSameParameters(matchingEventGroup)) {
                return matchingEventGroup;
            }
        }
        return null;
    }

    private static boolean haveSameValues(final PayloadAwareEntryCallEvent event1,
            final PayloadAwareEntryCallEvent event2) {
        return Arrays.equals(event1.getValues(), event2.getValues());
    }

    public InputPort<BehaviorModelGED> getReferenceModelInputPort() {
        return this.referenceModelInputPort;
    }

    public InputPort<BehaviorModelGED> getTestModelInputPort() {
        return this.testModelInputPort;
    }

    public OutputPort<BehaviorModelDifference> getOutputPort() {
        return this.resultPort;
    }

}
