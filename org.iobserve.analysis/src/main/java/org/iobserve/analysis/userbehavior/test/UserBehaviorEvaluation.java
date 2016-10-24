/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.userbehavior.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.impl.BranchImpl;
import org.palladiosimulator.pcm.usagemodel.impl.EntryLevelSystemCallImpl;
import org.palladiosimulator.pcm.usagemodel.impl.LoopImpl;
import org.palladiosimulator.pcm.usagemodel.impl.StartImpl;
import org.palladiosimulator.pcm.usagemodel.impl.StopImpl;

/**
 * It matches two usage models related to their model elements as well as the ordering of their
 * elements. The objective is to determine whether the two usage models correspond to each other.
 *
 * @author David Peter, Robert Heinrich
 */
public final class UserBehaviorEvaluation {

    /**
     * Default constructor.
     */
    private UserBehaviorEvaluation() {
    }

    /**
     * Executes the matching of the two passed usage models.
     *
     * @param createdUsageModel
     *            is matched against the reference usage model
     * @param referenceUsageModel
     *            is matched against the approach's generated usage model
     * @return returns accuracy results
     * @throws IOException
     *             on error
     */
    public static AccuracyResults matchUsageModels(final UsageModel createdUsageModel,
            final UsageModel referenceUsageModel) throws IOException {

        final AccuracyResults accuracyResults = new AccuracyResults();

        // Get the scenario behavior of each usage model
        final UsageScenario usageScenarioOfCreatedUsageModel = createdUsageModel.getUsageScenario_UsageModel().get(0);
        final UsageScenario usageScenarioOfReferenceUsageModel = referenceUsageModel.getUsageScenario_UsageModel()
                .get(0);
        final ScenarioBehaviour scenarioBehaviourOfCreatedUsageModel = usageScenarioOfCreatedUsageModel
                .getScenarioBehaviour_UsageScenario();
        final ScenarioBehaviour scenarioBehaviourOfReferenceUsageModel = usageScenarioOfReferenceUsageModel
                .getScenarioBehaviour_UsageScenario();

        // Creates for each usage model a list that contains the usage model's model elements
        final List<ModelElement> modelElementsOfCreatedUsageModel = new ArrayList<>();
        final List<ModelElement> modelElementsOfReferenceUsageModel = new ArrayList<>();
        UserBehaviorEvaluation.getModelElements(scenarioBehaviourOfCreatedUsageModel, modelElementsOfCreatedUsageModel);
        UserBehaviorEvaluation.getModelElements(scenarioBehaviourOfReferenceUsageModel,
                modelElementsOfReferenceUsageModel);

        // Calculate the evaluation metrics according to the extracted lists of model elements
        final double jc = UserBehaviorEvaluation.calculateJC(modelElementsOfCreatedUsageModel,
                modelElementsOfReferenceUsageModel);
        final double srcc = UserBehaviorEvaluation.calculateSpearmansCoefficient(modelElementsOfCreatedUsageModel,
                modelElementsOfReferenceUsageModel);
        accuracyResults.setJc(jc);
        accuracyResults.setSrcc(srcc);
        return accuracyResults;
    }

    /**
     * Calculates the JaccardCoefficient of the two lists that contain the model elements of the
     * usage models that are matched against each other. JC = Intersection(A,B) / Union(A,B), A =
     * List of model elements of the created usage model, B = list of model elements of the
     * reference usage model
     *
     * @param modelElementsOfCreatedUsageModel
     *            contains the model elements of the created usage model
     * @param modelElementsOfReferenceUsageModel
     *            contains the model elements of the reference usage model
     * @return
     */
    private static double calculateJC(final List<ModelElement> modelElementsOfCreatedUsageModel,
            final List<ModelElement> modelElementsOfReferenceUsageModel) {
        final List<ModelElement> modelElements1 = new ArrayList<>(modelElementsOfCreatedUsageModel);
        final List<ModelElement> modelElements2 = new ArrayList<>(modelElementsOfReferenceUsageModel);
        // Creates the intersection of the lists. Thereby, the elements of the intersection are
        // removed from both lists. Thus, after calculating the intersection both lists only contain
        // the elements that are not part of the intersection list. They are subsequently used to
        // calculate the union of both lists
        final List<ModelElement> intersectionList = UserBehaviorEvaluation.getIntersectionList(modelElements1,
                modelElements2);
        // Creates the union of the lists
        final List<ModelElement> unionList = UserBehaviorEvaluation.getUnionList(modelElements1, modelElements2,
                intersectionList);
        // Calculates the Jaccard Coefficient
        final double jc = (intersectionList.size() * 1.0) / (unionList.size() * 1.0);
        return jc;
    }

    /**
     * Creates the intersection of the passed lists.
     *
     * @param modelElementsOfCreatedUsageModel
     *            contains the model elements of the created usage model
     * @param modelElementsOfReferenceUsageModel
     *            contains the model elements of the reference usage model
     * @return intersection
     */
    private static List<ModelElement> getIntersectionList(final List<ModelElement> modelElementsOfCreatedUsageModel,
            final List<ModelElement> modelElementsOfReferenceUsageModel) {
        final List<ModelElement> intersectionList = new ArrayList<>();
        // Checks whether for each of the model elements of the created usage model there is a
        // corresponding model element within the reference usage model
        for (int i = 0; i < modelElementsOfCreatedUsageModel.size(); i++) {
            for (int j = 0; j < modelElementsOfReferenceUsageModel.size(); j++) {
                // If there is a match between two elements, the matching element is added to the
                // intersection list and the matching element is removed from both lists.
                // Thus, at the next loop run the model elements that are already matched are not
                // considered any more
                if (modelElementsOfCreatedUsageModel.get(i).equals(modelElementsOfReferenceUsageModel.get(j))) {
                    intersectionList.add(modelElementsOfCreatedUsageModel.get(i));
                    modelElementsOfCreatedUsageModel.remove(i);
                    i--; // NOCS
                    modelElementsOfReferenceUsageModel.remove(j);
                    break;
                }
            }
        }
        return intersectionList;
    }

    /**
     * Creates the union of the lists.
     *
     * @param modelElementsOfCreatedUsageModel
     *            contains the model elements of the created usage model that do not belong to the
     *            intersection
     * @param modelElementsOfReferenceUsageModel
     *            contains the model elements of the reference usage model that do not belong to the
     *            intersection
     * @param intersectionList
     *            contains the intersection of both lists
     * @return union
     */
    private static List<ModelElement> getUnionList(final List<ModelElement> modelElementsOfCreatedUsageModel,
            final List<ModelElement> modelElementsOfReferenceUsageModel, final List<ModelElement> intersectionList) {
        final List<ModelElement> unionList = new ArrayList<>();
        // The lists of the created and the reference usage model each contain only the model
        // elements that are not part of the intersection of both lists. Thus, the union consists of
        // the intersection and the rest of the model elements
        unionList.addAll(modelElementsOfCreatedUsageModel);
        unionList.addAll(modelElementsOfReferenceUsageModel);
        unionList.addAll(intersectionList);
        return unionList;
    }

    /**
     * Calculates the Spearman’s Rank Correlation Coefficient of the two lists that contain the
     * model elements of the usage models that are matched against each other. SRCC = 1 - ((6 *
     * SUM((rank distances)^2)/n(n^2-1)), n=number of model elements
     *
     * @param modelElementsOfUsageModel1
     *            contains the model elements of usage model 1
     * @param modelElementsOfUsageModel2
     *            contains the model elements of usage model 2
     * @return
     */
    private static double calculateSpearmansCoefficient(final List<ModelElement> modelElementsOfUsageModel1,
            final List<ModelElement> modelElementsOfUsageModel2) {

        final long numberOfModelElements = 1L * modelElementsOfUsageModel1.size();
        long squaredRank = 0;

        // Calculates for each model element the rank difference between matching model elements
        for (int i = 0; i < modelElementsOfUsageModel1.size(); i++) {
            for (int j = 0; j < modelElementsOfUsageModel2.size(); j++) {
                if (modelElementsOfUsageModel2.get(j) == null) {
                    continue;
                }
                if (modelElementsOfUsageModel1.get(i).equals(modelElementsOfUsageModel2.get(j))) {
                    squaredRank += (i - j) * (i - j);
                    modelElementsOfUsageModel2.set(j, null);
                    break;
                }
            }
        }
        // Calculates the SRCC
        final double srcc = 1 - ((6.0 * squaredRank)
                / (numberOfModelElements * ((numberOfModelElements * numberOfModelElements) - 1)));
        return srcc;
    }

    /**
     * Extracts for a scenario behavior the model elements.
     *
     * @param scenarioBehaviour
     *            whose model elements are extracted
     * @param modelElements
     *            the list to that the model elements are added
     */
    private static void getModelElements(final ScenarioBehaviour scenarioBehaviour,
            final List<ModelElement> modelElements) {

        final List<AbstractUserAction> actionsOfScenarioBehaviour = scenarioBehaviour.getActions_ScenarioBehaviour();
        AbstractUserAction nextActionOfScenarioBehaviour = actionsOfScenarioBehaviour.get(0);

        // Adds model elements to the list of model elements while there is a successor model
        // element
        while (nextActionOfScenarioBehaviour != null) {

            if (nextActionOfScenarioBehaviour.getClass().equals(StartImpl.class)) {
                final ModelElement modelElement = new ModelElement(true, false, false, false, false, "", "", 0);
                modelElements.add(modelElement);
            } else if (nextActionOfScenarioBehaviour.getClass().equals(StopImpl.class)) {
                final ModelElement modelElement = new ModelElement(false, true, false, false, false, "", "", 0);
                modelElements.add(modelElement);
            } else if (nextActionOfScenarioBehaviour.getClass().equals(EntryLevelSystemCallImpl.class)) {
                final EntryLevelSystemCall call = (EntryLevelSystemCall) nextActionOfScenarioBehaviour;
                final ModelElement modelElement = new ModelElement(false, false, true, false, false,
                        call.getEntityName(), "", 0);
                modelElements.add(modelElement);
            } else if (nextActionOfScenarioBehaviour.getClass().equals(LoopImpl.class)) {
                final Loop loop = (Loop) nextActionOfScenarioBehaviour;
                final ModelElement modelElement = new ModelElement(false, false, false, false, true, "",
                        loop.getLoopIteration_Loop().getSpecification(), 0);
                modelElements.add(modelElement);
                UserBehaviorEvaluation.getModelElements(loop.getBodyBehaviour_Loop(), modelElements);
            } else if (nextActionOfScenarioBehaviour.getClass().equals(BranchImpl.class)) {
                final Branch branch = (Branch) nextActionOfScenarioBehaviour;
                // Because the branch transitions of the usage models are not always in the same
                // order, we order the branch transitions by the entity name of their first
                // EntryLevelSystemCall. In doing so, the model elements of each usage model are
                // added in the same order independently of the ordering of the usage model. The
                // ordering of the branch transitions does not affect the accuracy of an usage model
                // and is created randomly within the usage model and can not be influenced.
                final List<BranchTransition> branchTransitionsSorted = UserBehaviorEvaluation
                        .sortBranchTransitions(branch.getBranchTransitions_Branch());
                for (int i = 0; i < branchTransitionsSorted.size(); i++) {
                    final ModelElement modelElement = new ModelElement(false, false, false, true, false, "", "",
                            branchTransitionsSorted.get(i).getBranchProbability());
                    modelElements.add(modelElement);
                    UserBehaviorEvaluation.getModelElements(
                            branchTransitionsSorted.get(i).getBranchedBehaviour_BranchTransition(), modelElements);
                }
            }

            // Gets the successor model element
            nextActionOfScenarioBehaviour = nextActionOfScenarioBehaviour.getSuccessor();
        }

    }

    /**
     * It sorts a list of branch transitions by their first EntryLevelSystemCall.
     *
     * @param branchTransitions
     *            that are sorted
     * @return sorted list of branch transitions
     */
    private static List<BranchTransition> sortBranchTransitions(final List<BranchTransition> branchTransitions) {
        final List<String> entityNames = new ArrayList<>();
        final List<BranchTransition> branchTransitionsSorted = new ArrayList<>();
        for (final BranchTransition branchTransition : branchTransitions) {
            final EntryLevelSystemCall call = (EntryLevelSystemCall) branchTransition
                    .getBranchedBehaviour_BranchTransition().getActions_ScenarioBehaviour().get(0).getSuccessor();
            entityNames.add(call.getEntityName());
        }
        java.util.Collections.sort(entityNames);
        for (final String entityName : entityNames) {
            for (final BranchTransition branchTransition : branchTransitions) {
                final EntryLevelSystemCall call = (EntryLevelSystemCall) branchTransition
                        .getBranchedBehaviour_BranchTransition().getActions_ScenarioBehaviour().get(0).getSuccessor();
                if (call.getEntityName().equals(entityName)) {
                    branchTransitionsSorted.add(branchTransition);
                }
            }
        }

        return branchTransitionsSorted;
    }

}
