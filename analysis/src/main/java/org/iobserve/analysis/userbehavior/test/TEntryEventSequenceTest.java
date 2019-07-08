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

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.correspondence.CorrespondeceModelFactory;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.userbehavior.UserBehaviorTransformation;
import org.iobserve.analysis.userbehavior.test.builder.BranchWithinBranchReference;
import org.iobserve.analysis.userbehavior.test.builder.BranchWithinBranchReference2;
import org.iobserve.analysis.userbehavior.test.builder.BranchWithinLoopReference;
import org.iobserve.analysis.userbehavior.test.builder.CallSequenceScalabilityReference;
import org.iobserve.analysis.userbehavior.test.builder.LoopWithinBranchReference;
import org.iobserve.analysis.userbehavior.test.builder.LoopWithinLoopReference;
import org.iobserve.analysis.userbehavior.test.builder.OverlappingIterationReference;
import org.iobserve.analysis.userbehavior.test.builder.SimpleBranchReference;
import org.iobserve.analysis.userbehavior.test.builder.SimpleLoopReference;
import org.iobserve.analysis.userbehavior.test.builder.SimpleSequenceReference;

/**
 * Test of the TEntryEventSequence filter.
 *
 * @author David Peter, Robert Heinrich, Nicolas Boltz
 */
public final class TEntryEventSequenceTest {

    private static final int THINK_TIME = 1;
    private static final int NUMBER_OF_USER_GROUPS = 1;
    private static final boolean CLOSED_WORKLOAD = true;
    private static final int VARIANCE_OF_USER_GROUPS = 0;
    private static final int NUMBER_OF_ITERATIONS_PER_TEST = 5000;//1, 500;
    private static final int STEP_SIZE = 1;

    private static final String TEST_FOLDER = "D:\\Dokumente\\Uni\\HiWi\\UsageBehaviourTransformationTest\\TEntryEventSequenceTest\\";
    private static final String TEST_DATA_FOLDER = TEST_FOLDER + "data\\";
    private static final String TEST_RESULTS_FOLDER = TEST_FOLDER + "results\\";
    private static final String OUTPUT_USAGE_MODEL = TEST_DATA_FOLDER + "OutputModel.usagemodel";
    private static final String REFERENCE_USAGE_MODEL = TEST_DATA_FOLDER + "\\ReferenceModel.usagemodel";
    private static final String REPOSITORY_MODEL_PATH = TEST_DATA_FOLDER + "cocome-cloud.repository";
    private static final String CORRESPONDENCE_MODEL_PATH = TEST_DATA_FOLDER + "mapping.rac";

    private static RepositoryModelProvider repositoryModelProvider = null;
    private static ICorrespondence correspondenceModel = null;
    
    /**
     * Test class.
     */
    public TEntryEventSequenceTest() {
    	repositoryModelProvider = new RepositoryModelProvider(URI.createFileURI(REPOSITORY_MODEL_PATH));
    	correspondenceModel = CorrespondeceModelFactory.INSTANCE.createCorrespondenceModel(CORRESPONDENCE_MODEL_PATH);
    }
    
    public void startTests() {
    	try {
	    	System.out.println("Start SimpleSequence test");
	    	testSimpleSequence();
	    	System.out.println("Start SimpleLoop test");
	    	testSimpleLoop();
	    	System.out.println("Start SimpleBranch test");
	    	testSimpleBranch();
	    	System.out.println("Start OverlappingLoops test");
	    	testOverlappingIteration();
	    	System.out.println("Start LoopWithinLoop test");
	    	testLoopWithinLoop();
			System.out.println("Start LoopWithinBranch test");
	    	testLoopWithinBranch();
	    	System.out.println("Start BranchWithinLoop test");
			testBranchWithinLoop();
	    	System.out.println("Start BranchWithinBranch test");
	    	testBranchWithinBranch();


		
		} catch (IOException e) {
			System.out.println("IO Error in testing. Maybe testdata missing/paths incorrect.");
			e.printStackTrace();
		}
    }
    
    public void startRMETest() {
    	try {
	    	System.out.println("Start SimpleSequence RME openWorkload test");
	    	testSimpleSequenceRME(false);
	    	
	    	System.out.println("Start SimpleSequence RME closedWorkload test");
			testSimpleSequenceRME(true);
		} catch (IOException e) {
			System.out.println("IO Error in testing RME. Maybe testdata missing/paths incorrect.");
			e.printStackTrace();
		}
    }
    
    public void testSimpleSequence() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = SimpleSequenceReference.getModel(
                    REFERENCE_USAGE_MODEL, repositoryModelProvider, correspondenceModel,
                    THINK_TIME, CLOSED_WORKLOAD);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "SimpleSequenceAccuracy");
    }
    
    public void testSimpleBranch() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = SimpleBranchReference.getModel(
                    REFERENCE_USAGE_MODEL ,repositoryModelProvider, correspondenceModel);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "SimpleBranchAccuracy");
    }
    
    public void testSimpleLoop() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = SimpleLoopReference.getModel(
                    REFERENCE_USAGE_MODEL ,repositoryModelProvider, correspondenceModel);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "SimpleLoopAccuracy");
    }
    
    public void testOverlappingIteration() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = OverlappingIterationReference.getModel(
                    REFERENCE_USAGE_MODEL ,repositoryModelProvider, correspondenceModel);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "OverlappingIterationAccuracy");
    }
    
    public void testBranchWithinBranch() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = BranchWithinBranchReference2.getModel(
                    REFERENCE_USAGE_MODEL ,repositoryModelProvider, correspondenceModel);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "BranchWithinBranchAccuracy");
    }
    
    public void testLoopWithinBranch() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = LoopWithinBranchReference.getModel(
                    REFERENCE_USAGE_MODEL ,repositoryModelProvider, correspondenceModel);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "LoopWithinBranchAccuracy");
    }
    
    public void testLoopWithinLoop() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = LoopWithinLoopReference.getModel(
                    REFERENCE_USAGE_MODEL ,repositoryModelProvider, correspondenceModel);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "LoopWithinLoopAccuracy");
    }
    
    public void testBranchWithinLoop() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = BranchWithinLoopReference.getModel(
                    REFERENCE_USAGE_MODEL ,repositoryModelProvider, correspondenceModel);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            if (accuracyResults.getJc() != 1.0 || accuracyResults.getSrcc() != 1.0) {
            	System.out.println("Error in Usage Modelling");
            }
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "BranchWithinLoopAccuracy");
    }
    
    public void testSimpleSequenceRME(boolean closedWorkload) throws IOException {
        List<Double> resultsRME = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = SimpleSequenceReference.getModel(
                    REFERENCE_USAGE_MODEL, repositoryModelProvider, correspondenceModel,
                    THINK_TIME, closedWorkload);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    closedWorkload, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
            
            double relativeMeasurementError = WorkloadEvaluation.calculateRME(behaviorModeling.getPcmUsageModel(),
                    referenceElements);
            
            resultsRME.add(relativeMeasurementError);

            
        }

        TestHelper.writeRME(resultsRME, TEST_RESULTS_FOLDER + "SimpleSequenceRME_closedWorkload_" + closedWorkload);
    }
    
    // Obviously not Used for accuracy testing
    public void testCallSequenceScalability() throws IOException {
        List<AccuracyResults> results = new ArrayList<>();
        
        for (int i = 1; i <= NUMBER_OF_ITERATIONS_PER_TEST; i += STEP_SIZE) {
            ReferenceElements referenceElements = CallSequenceScalabilityReference.getModel(100);
            
            UserBehaviorTransformation behaviorModeling = new UserBehaviorTransformation(
                    referenceElements.getEntryCallSequenceModel(), NUMBER_OF_USER_GROUPS, VARIANCE_OF_USER_GROUPS,
                    CLOSED_WORKLOAD, THINK_TIME, repositoryModelProvider, correspondenceModel);
            behaviorModeling.modelUserBehavior();

            AccuracyResults accuracyResults = UserBehaviorEvaluation
                    .matchUsageModels(behaviorModeling.getPcmUsageModel(), referenceElements.getUsageModel());
            results.add(accuracyResults);

            TestHelper.saveModel(behaviorModeling.getPcmUsageModel(), OUTPUT_USAGE_MODEL);
        }

        TestHelper.writeAccuracyResults(results, TEST_RESULTS_FOLDER + "CallSequenceScalabilityAccuracy");
    }
}
