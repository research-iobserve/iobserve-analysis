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
 * Represents the intersection model generation strategy
 *
 * @author Jannis Kuckei
 *
 */
public class IntersectionModelGenerationStrategy implements IModelGenerationStrategy {

    @Override
    public BehaviorModel generateModel(final BehaviorModel[] models) {
        if (models.length == 0) {
            return new BehaviorModel();
        }

        BehaviorModel result = models[0];
        for (int i = 1; i < models.length; i++) {
            result = this.intersect(result, models[i]);
        }

        return result;
    }

    private BehaviorModel intersect(final BehaviorModel a, final BehaviorModel b) {
        final BehaviorModel result = new BehaviorModel();

        Optional<EntryCallEdge> matchingEdge;
        for (final EntryCallEdge edgeA : a.getEdges()) {
            matchingEdge = b.findEdge(edgeA.getSource().getSignature(), edgeA.getTarget().getSignature());
            if (matchingEdge.isPresent()) {
                final EntryCallNode newSource = this.intersectNode(edgeA.getSource(), matchingEdge.get().getSource());
                final EntryCallNode newTarget = this.intersectNode(edgeA.getTarget(), matchingEdge.get().getTarget());
                final int edgeCount = (int) Math.min(edgeA.getCalls(), matchingEdge.get().getCalls());
                final EntryCallEdge newEdge = new EntryCallEdge(newSource, newTarget, edgeCount);

                result.addEdge(newEdge, false);
            }
        }

        return result;
    }

    private EntryCallNode intersectNode(final EntryCallNode a, final EntryCallNode b) {
        final EntryCallNode result = new EntryCallNode();
        result.setSignature(a.getSignature());

        Optional<CallInformation> matchingInfo;
        for (final CallInformation infoA : a.getEntryCallInformation()) {
            matchingInfo = b.findCallInformation(infoA.getInformationSignature(), infoA.getInformationParameter());
            if (matchingInfo.isPresent()) {
                final int newCalls = Math.min(infoA.getCount(), matchingInfo.get().getCount());
                final CallInformation newInfo = new CallInformation(infoA.getInformationSignature(),
                        infoA.getInformationParameter(), newCalls);
                result.mergeInformation(newInfo);
            }
        }

        return result;
    }
}
