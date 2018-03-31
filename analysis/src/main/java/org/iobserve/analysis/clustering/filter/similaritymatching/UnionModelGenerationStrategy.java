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
package org.iobserve.analysis.clustering.filter.similaritymatching;

import java.util.Optional;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.clustering.behaviormodels.CallInformation;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallEdge;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallNode;

/**
 * Generates representative models by joining them together
 *
 * @author Jannis Kuckei
 *
 */
public class UnionModelGenerationStrategy implements IModelGenerationStrategy {

    @Override
    public BehaviorModel generateModel(final BehaviorModel[] models) {
        final BehaviorModel newModel = new BehaviorModel();

        for (final BehaviorModel model : models) {
            /** We add nodes first so we don't get duplicate nodes from the same model */
            for (final EntryCallNode node : model.getNodes()) {
                final Optional<EntryCallNode> matchingNode = newModel.findNode(node.getSignature());

                if (matchingNode.isPresent()) {
                    Optional<CallInformation> matchingInfo;
                    for (final CallInformation info : node.getEntryCallInformation()) {
                        matchingInfo = matchingNode.get().findCallInformation(info.getInformationSignature(),
                                info.getInformationParameter());
                        if (matchingInfo.isPresent()) {
                            final int newCount = Math.max(info.getCount(), matchingInfo.get().getCount());
                            matchingInfo.get().setCount(newCount);
                        } else {
                            matchingNode.get().mergeInformation(new CallInformation(info.getInformationSignature(),
                                    info.getInformationParameter(), info.getCount()));
                        }
                    }
                } else {
                    // Copy node and add it
                    final EntryCallNode newNode = new EntryCallNode();
                    newNode.setSignature(node.getSignature());
                    for (final CallInformation info : node.getEntryCallInformation()) {
                        final CallInformation newInfo = new CallInformation(info.getInformationSignature(),
                                info.getInformationParameter(), info.getCount());
                        newNode.mergeInformation(newInfo);
                    }
                    newModel.addNode(newNode, false);
                }

            }
            for (final EntryCallEdge edge : model.getEdges()) {
                final Optional<EntryCallEdge> matchingEdge = newModel.findEdge(edge.getSource().getSignature(),
                        edge.getTarget().getSignature());
                if (matchingEdge.isPresent()) {
                    matchingEdge.get().setCalls(Math.max(matchingEdge.get().getCalls(), edge.getCalls()));
                } else {
                    newModel.addEdge(new EntryCallEdge(edge.getSource(), edge.getTarget(), edge.getCalls()), false);
                }
            }
        }

        return newModel;
    }

}
