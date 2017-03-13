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
package org.iobserve.analysis.cdoruserbehavior.filter;

import org.iobserve.analysis.cdoruserbehavior.filter.models.AbstractBehaviorModelTable;
import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.CallInformation;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallEdge;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 *
 */
public class TBehaviorModelCreation extends AbstractConsumerStage<Instances> {

    private final OutputPort<BehaviorModel> outputPort = this.createOutputPort();

    @Override
    protected void execute(Instances instances) {

        final int size = instances.numInstances();

        for (int i = 0; i < size; i++) {
            final Instance instance = instances.instance(i);
            final BehaviorModel behaviorModel = this.createBehaviorModel(instances, instance);
            this.outputPort.send(behaviorModel);
        }
    }

    /**
     * getter
     *
     * @return the outputPort
     */
    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }

    /**
     * create a BehaviorModel from Instance
     *
     * @param instances
     *            instances containing the attribute names
     * @param instance
     *            instance containing the attributes
     * @return behavior model
     */
    private BehaviorModel createBehaviorModel(final Instances instances, final Instance instance) {
        final int size = instance.numAttributes();
        final BehaviorModel behaviorModel = new BehaviorModel();

        for (int i = 0; i < size; i++) {
            final Attribute attribute = instances.attribute(i);

            final String attributeName = attribute.name();
            final Double attributeValue = instance.value(attribute);

            if (this.matchEdge(attributeName)) {
                final EntryCallEdge edge = this.createEdge(attributeName, attributeValue);
                behaviorModel.addEdge(edge);

            } else if (this.matchNode(attributeName)) {
                final EntryCallNode node = this.createNode(attributeName, attributeValue);
                behaviorModel.addNode(node);
            }
        }
        return behaviorModel;
    }

    /**
     * Does the attribute name represent an edge?
     *
     * @param name
     *            attribute name
     * @return true if the name represents an edge, false else
     */
    private boolean matchEdge(String name) {
        return name.startsWith(AbstractBehaviorModelTable.EDGE_INDICATOR);
    }

    /**
     * Does the attribute name represent a node?
     *
     * @param name
     *            attribute name
     * @return true if the name represents an node, false else
     */
    private boolean matchNode(String name) {
        return name.startsWith(AbstractBehaviorModelTable.INFORMATION_INDICATOR);
    }

    /**
     * creates an edge from an edge representing attribute string and value
     *
     * @param name
     *            attribute name
     * @param value
     *            attribute value
     *
     * @return EntryCallEdge
     */
    private EntryCallEdge createEdge(String name, Double value) {

        final String[] nodeNames = this.splitSignature(AbstractBehaviorModelTable.EDGE_INDICATOR,
                AbstractBehaviorModelTable.EDGE_DIVIDER, name);

        if (nodeNames.length == 2) {
            final EntryCallNode from = new EntryCallNode(nodeNames[0]);

            final EntryCallNode to = new EntryCallNode(nodeNames[1]);

            final EntryCallEdge edge = new EntryCallEdge(from, to, value.intValue());

            return edge;
        } else {
            return null;
        }

    }

    /**
     * creates an node from a node representing attribute string and value
     *
     * @param name
     *            attribute name
     * @param value
     *            attribute value
     *
     * @return EntryCallNode
     */
    private EntryCallNode createNode(String name, Double value) {

        final String[] signatures = this.splitSignature(AbstractBehaviorModelTable.INFORMATION_INDICATOR,
                AbstractBehaviorModelTable.INFORMATION_DIVIDER, name);

        if (signatures.length == 2) {
            final EntryCallNode node = new EntryCallNode(signatures[0]);
            final CallInformation callInformation = new CallInformation(signatures[1], value);

            node.getEntryCallInformation().add(callInformation);

            return node;
        } else {
            return null;
        }
    }

    /**
     * splits the signature
     *
     * @param indicator
     *            indicator
     * @param divider
     *            divider
     * @param signature
     *            signature
     * @return separate strings
     */
    private String[] splitSignature(String indicator, String divider, String signature) {

        return signature.replaceAll(indicator, "").split(divider);

    }

}
