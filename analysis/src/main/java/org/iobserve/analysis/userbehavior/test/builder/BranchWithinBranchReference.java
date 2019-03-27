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
package org.iobserve.analysis.userbehavior.test.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.CorrespondenceModelProvider;
import org.iobserve.analysis.model.correspondence.ArchitecturalModelElement;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.ReferenceElements;
import org.iobserve.analysis.userbehavior.test.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.TestHelper;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

/**
 * BranchWithinBranchReference.
 *
 * @author Nicolas Boltz
 */
public final class BranchWithinBranchReference {

    /**
     * Factory.
     */
    private BranchWithinBranchReference() {

    }

    /**
     * It creates a reference usage model that contains nested branches. Accordingly, user sessions
     * whose call sequences differ from each other at the positions of the branches are
     * created.(RQ-1.5)
     *
     * @param referenceUsageModelFileName
     *            file name of the reference model to store its result
     * @param usageModelBuilder
     *            usage model builder
     * @param repositoryModelProvider
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     *
     * @return a reference usage model and corresponding user sessions
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
    		final RepositoryModelProvider repositoryModelProvider,
    		final CorrespondenceModelProvider correspondenceModelProvider) throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model. The minimum number of user sessions is set
        // dependently from the random number of branch transitions, because it must be ensured that
        // each branch transition is represented within the user sessions.
        final int numberOfTransitionsOfExteriorBranch = TestHelper.getRandomInteger(3, 2);
        final int numberOfTransitionsOfInteriorBranches = TestHelper.getRandomInteger(3, 2);
        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(200, 10 * numberOfTransitionsOfExteriorBranch);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));
        
        ArrayList<Integer> exteriorCallIds = new ArrayList<Integer>();
        int[] exteriorCallAmounts = new int[numberOfTransitionsOfExteriorBranch];
        ArrayList<List<Integer>> interiorCallIds = new ArrayList<>();
        ArrayList<int[]> interiorCallAmounts = new ArrayList<>();
        
        for(int i = 0; i < numberOfTransitionsOfExteriorBranch; i++) {
        	//create 2-3 random exterior calls
        	int operationSignature;
        	do
        	{
        		operationSignature = TestHelper.getRandomInteger(49,0) % 5;
        	} while(exteriorCallIds.contains(operationSignature));
        	
        	exteriorCallIds.add(operationSignature);
        	exteriorCallAmounts[i] = 0;
        	
        	// create 2-3 interior calls for each
        	// Create unique interior calls for each exterior call to prevent compacting of branches
        	List<Integer> branchInteriorCallIds;
        	boolean uniqueInteriorCalls;
        	do
        	{
        		branchInteriorCallIds = createRandomInternalTransitionCalls(numberOfTransitionsOfInteriorBranches);
        		uniqueInteriorCalls = true;
            	for(int x = 0; x < i; x++) {
            		if(compareCallLists(branchInteriorCallIds, interiorCallIds.get(x))) {
            			uniqueInteriorCalls = false;
            			break;
            		}
            	}
        	} while(!uniqueInteriorCalls);
        	
        	interiorCallIds.add(branchInteriorCallIds);
        	
        	// Initialize interiorCallAmounts
        	interiorCallAmounts.add(new int[numberOfTransitionsOfInteriorBranches]);
        	for(int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
            	interiorCallAmounts.get(i)[k] = 0;
            }
        }
        
        // optional -> Test, dass interior calls zweier pfade nicht gleich sind
        
        final ReferenceElements referenceElements = new ReferenceElements();

        BranchWithinBranchReference.createUserSessions(exteriorCallIds, exteriorCallAmounts, interiorCallIds, interiorCallAmounts,
                numberOfTransitionsOfExteriorBranch, numberOfTransitionsOfInteriorBranches, entryCallSequenceModel);

        final UsageModel usageModel = BranchWithinBranchReference.createTheReferenceModel(repositoryModelProvider, 
        		correspondenceModelProvider, numberOfTransitionsOfExteriorBranch, numberOfTransitionsOfInteriorBranches, 
        		numberOfConcurrentUsers, exteriorCallIds, exteriorCallAmounts, interiorCallIds, interiorCallAmounts);

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }
    
    private static List<Integer> createRandomInternalTransitionCalls(int numberOfInternalTransitions) {
    	ArrayList<Integer> branchInteriorCallId = new ArrayList<Integer>();
    	for(int k = 0; k < numberOfInternalTransitions; k++) {
        	int internalOperationSignature;
        	do
        	{
        		internalOperationSignature = TestHelper.getRandomInteger(49,0) % 5;
        	} while(branchInteriorCallId.contains(internalOperationSignature));
        	
        	branchInteriorCallId.add(internalOperationSignature);
        	
        }
    	
    	return branchInteriorCallId;
    }
    
    private static boolean compareCallLists(List<Integer> list1, List<Integer> list2) {
    	if(list1.size() == list2.size()) {
    		return list1.containsAll(list2);
    	}
    	
    	return false;
    }

    /**
     * User sessions are created that exactly represent the user behavior of the reference usage
     * model. The entry and exit times enable that the calls within the user sessions are ordered
     * according to the reference usage model. The branch transition counter ensures that each
     * branch transition is visited by at least one user session
     *
     * @param branchTransitionCounter
     * @param listOfbranchTransitionCounterInterior
     * @param numberOfTransitionsOfExteriorBranch
     * @param numberOfTransitionsOfInteriorBranches
     * @param entryCallSequenceModel
     */
    private static void createUserSessions(final List<Integer> exteriorCallIds, int[] exteriorCallAmounts,
    		final List<List<Integer>> interiorCallIds, List<int[]> interiorCallAmounts,
            final int numberOfTransitionsOfExteriorBranch, final int numberOfTransitionsOfInteriorBranches,
            final EntryCallSequenceModel entryCallSequenceModel) {
    	
    	// Do each call path at least once
    	int minimumSessionAmount = 0;
    	for(int exteriorSessionIt = 0; exteriorSessionIt < exteriorCallIds.size(); exteriorSessionIt++) {
    		for(int interiorSessionIt = 0; interiorSessionIt < interiorCallIds.get(exteriorSessionIt).size(); interiorSessionIt++) {
    			int entryTime = 1;
            	UserSession session = entryCallSequenceModel.getUserSessions().get(minimumSessionAmount);
            	
            	int exteriorBranchId = exteriorSessionIt;
            	exteriorCallAmounts[exteriorBranchId]++;
            	final EntryCallEvent externalBranchEvent = new EntryCallEvent(entryTime, entryTime + 1,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[exteriorCallIds.get(exteriorBranchId)],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[exteriorCallIds.get(exteriorBranchId)], String.valueOf(minimumSessionAmount), "hostname");
            	session.add(externalBranchEvent, true);
            	
            	int internalBranchId = interiorSessionIt;
            	interiorCallAmounts.get(exteriorBranchId)[internalBranchId]++;
            	final EntryCallEvent internalBranchEvent = new EntryCallEvent(entryTime + 2, entryTime + 3,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[interiorCallIds.get(exteriorBranchId).get(internalBranchId)],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[interiorCallIds.get(exteriorBranchId).get(internalBranchId)], String.valueOf(minimumSessionAmount), "hostname");
            	session.add(internalBranchEvent, true);
            	minimumSessionAmount++;
    		}
    	}
    	
    	
        for(int sessionIt = minimumSessionAmount; sessionIt < entryCallSequenceModel.getUserSessions().size(); sessionIt++) {
        	int entryTime = 1;
        	UserSession session = entryCallSequenceModel.getUserSessions().get(sessionIt);
        	
        	int exteriorBranchId = TestHelper.getRandomInteger(numberOfTransitionsOfExteriorBranch - 1, 0);
        	exteriorCallAmounts[exteriorBranchId]++;
        	final EntryCallEvent externalBranchEvent = new EntryCallEvent(entryTime, entryTime + 1,
                    ReferenceUsageModelBuilder.OPERATION_SIGNATURE[exteriorCallIds.get(exteriorBranchId)],
                    ReferenceUsageModelBuilder.CLASS_SIGNATURE[exteriorCallIds.get(exteriorBranchId)], String.valueOf(sessionIt), "hostname");
        	session.add(externalBranchEvent, true);
        	
        	int internalBranchId = TestHelper.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
        	interiorCallAmounts.get(exteriorBranchId)[internalBranchId]++;
        	final EntryCallEvent internalBranchEvent = new EntryCallEvent(entryTime + 2, entryTime + 3,
                    ReferenceUsageModelBuilder.OPERATION_SIGNATURE[interiorCallIds.get(exteriorBranchId).get(internalBranchId)],
                    ReferenceUsageModelBuilder.CLASS_SIGNATURE[interiorCallIds.get(exteriorBranchId).get(internalBranchId)], String.valueOf(sessionIt), "hostname");
        	session.add(internalBranchEvent, true);
        }
    }

    /**
     * Creates the reference model.
     *
     * @param usageModelBuilder
     * @param correspondenceModel
     * @param numberOfTransitionsOfExteriorBranch
     * @param numberOfTransitionsOfInteriorBranches
     * @param numberOfConcurrentUsers
     * @param branchTransitionCounter
     * @param listOfbranchTransitionCounterInterior
     * @return
     */
    private static UsageModel createTheReferenceModel(final RepositoryModelProvider repositoryModelProvider, 
    		final CorrespondenceModelProvider correspondenceModelProvider, final int numberOfTransitionsOfExteriorBranch,
    		final int numberOfTransitionsOfInteriorBranches, final int numberOfConcurrentUsers, 
    		final List<Integer> exteriorCallIds, int[] exteriorCallAmounts,
    		final List<List<Integer>> interiorCallIds, List<int[]> interiorCallAmounts) {
        // In the following the reference usage model is created
    	Optional<ArchitecturalModelElement> architecturalModelElement;
        final UsageModel referenceModel = UsageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = UsageModelBuilder.createUsageScenario("", referenceModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = UsageModelBuilder.createAddStartAction("", scenarioBehaviour);
        final Stop stop = UsageModelBuilder.createAddStopAction("", scenarioBehaviour);
        
     // The exterior branch is created
        final org.palladiosimulator.pcm.usagemodel.Branch branch = UsageModelBuilder.createBranch("", scenarioBehaviour);
        UsageModelBuilder.connect(start, branch);
        UsageModelBuilder.connect(branch, stop);
        
        for(int i = 0; i < numberOfTransitionsOfExteriorBranch; i++) {
        	final BranchTransition branchTransition = UsageModelBuilder.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition.getBranchedBehaviour_BranchTransition();
            branchTransition.setBranchProbability((double) exteriorCallAmounts[i] / (double) numberOfConcurrentUsers);
            final Start startBranchTransition = UsageModelBuilder.createStart("");
            UsageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
            final Stop stopBranchTransition = UsageModelBuilder.createStop("");
            UsageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
			
            architecturalModelElement = correspondenceModelProvider.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[i],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[i]);
            final org.palladiosimulator.pcm.usagemodel.Branch internalBranch = UsageModelBuilder.createBranch("", branchTransitionBehaviour);
            if (architecturalModelElement.isPresent()) {
                final EntryLevelSystemCall entryLevelSystemCall = UsageModelBuilder
                        .createEntryLevelSystemCall(repositoryModelProvider, architecturalModelElement.get());
                UsageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                UsageModelBuilder.connect(startBranchTransition, entryLevelSystemCall);
                UsageModelBuilder.connect(entryLevelSystemCall, internalBranch);
                UsageModelBuilder.connect(internalBranch, stopBranchTransition);
            }

            for(int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
            	final BranchTransition internalBranchTransition = UsageModelBuilder.createBranchTransition(internalBranch);
                final ScenarioBehaviour internalBranchTransitionBehaviour = internalBranchTransition.getBranchedBehaviour_BranchTransition();
                internalBranchTransition.setBranchProbability((double) interiorCallAmounts.get(i)[k] / (double) exteriorCallAmounts[i]);
                final Start startInternalBranchTransition = UsageModelBuilder.createStart("");
                UsageModelBuilder.addUserAction(internalBranchTransitionBehaviour, startInternalBranchTransition);
                final Stop stopInternalBranchTransition = UsageModelBuilder.createStop("");
                UsageModelBuilder.addUserAction(internalBranchTransitionBehaviour, stopInternalBranchTransition);
                architecturalModelElement = correspondenceModelProvider.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[interiorCallIds.get(i).get(k)],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[interiorCallIds.get(i).get(k)]);
                if (architecturalModelElement.isPresent()) {
                    final EntryLevelSystemCall entryLevelSystemCall = UsageModelBuilder
                            .createEntryLevelSystemCall(repositoryModelProvider, architecturalModelElement.get());
                    UsageModelBuilder.addUserAction(internalBranchTransitionBehaviour, entryLevelSystemCall);
                    UsageModelBuilder.connect(startInternalBranchTransition, entryLevelSystemCall);
                    UsageModelBuilder.connect(entryLevelSystemCall, stopInternalBranchTransition);
                }
            }
        }

        return referenceModel;
    }
}
