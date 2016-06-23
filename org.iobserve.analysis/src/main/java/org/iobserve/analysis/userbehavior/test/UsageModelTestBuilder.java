package org.iobserve.analysis.userbehavior.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.Workload;

import com.google.common.base.Optional;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.UsageModelBuilder;

public class UsageModelTestBuilder {
	private final UsageModelBuilder usageModelBuilder;
	private final ICorrespondence correspondenceModel;
	private final String usageModelFolder = "/Users/David/GitRepositories/iObserve/org.iobserve.analysis/output/usageModels/";
	private final String classSignature1 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.BookSale";
	private final String operationSignature1 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.BookSale.Get ()";
	private final String classSignature2 = "tradingsystem_inventory_application_store.ejb.ITradingSystem_Inventory_Application_Store";
	private final String operationSignature2 = "tradingsystem_inventory_application_store.ejb.ITradingSystem_Inventory_Application_Store.storeIf_flushDatabase9()";
	private final String classSignature3 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStoreById";
	private final String operationSignature3 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStoreById.Get()";
	private final String classSignature4 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStockItem";
	private final String operationSignature4 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStockItem.Get()";
	private final String classSignature5 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryLowStockItemsWithRespectToIncomingProducts";
	private final String operationSignature5 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryLowStockItemsWithRespectToIncomingProducts.Get()";
	
	
	public UsageModelTestBuilder() {
		this.usageModelBuilder = new UsageModelBuilder(AnalysisMain.getInstance().getModelProviderPlatform().getUsageModelProvider());
		this.correspondenceModel = AnalysisMain.getInstance().getModelProviderPlatform().getCorrespondenceModel();
	}
	
	public EntryCallSequenceModel getIncreasingUserSessionsScalabilityTestModel(int numberOfUserSessions) {

		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfUserSessions));
		
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
						
			EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
			entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
//			EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
//			entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
//			EntryCallEvent entryCallEvent3 = new EntryCallEvent(5, 6, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
//			entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent3, true);
			
		}		
		
		return entryCallSequenceModel;
	}
	
	public EntryCallSequenceModel getIncreasingCallSequenceScalabilityTestModel(int numberOfCalls) {
		int numberOfUserSessions = 10;
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfUserSessions));
		
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			int entryTime = 1;
			int exitTime = 2;
			for(int j=0;j<numberOfCalls;j++) {
			
				int callChooser = this.getRandomInteger(4, 0);
				
				if(callChooser==0) {
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				} else if(callChooser==1) {
					EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
				} else if(callChooser==2) {
					EntryCallEvent entryCallEvent3 = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent3, true);
				} else if(callChooser==3) {
					EntryCallEvent entryCallEvent4 = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent4, true);
				} else {
					EntryCallEvent entryCallEvent5 = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent5, true);
				}
				
				entryTime +=2;
				exitTime +=2;
			
			}
		}		
		
		return entryCallSequenceModel;
	}
	
	public TestElements getSimpleSequenceTestModel(int thinkTime, boolean isClosedWorkload) throws IOException {

		int numberOfConcurrentUsers = this.getRandomInteger(200, 20);
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		int numberOfCalls = this.getRandomInteger(5, 1);
		AbstractUserAction lastAction;
		
		/*
		 * Usage model basics 
		 */
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		if(isClosedWorkload)
			this.usageModelBuilder.createClosedWorkload(numberOfConcurrentUsers, thinkTime, usageScenario);
		else 
			this.usageModelBuilder.createOpenWorkload(0, usageScenario);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		lastAction = start;
		
		/*
		 * Creates the sequence 
		 * Corresponding to the model the call sequences of the user sessions are created
		 */
		long entryTime = 1;
		long exitTime = 2;
		Optional<Correspondent> optionCorrespondent;
		for(int i=0;i<numberOfCalls;i++) {
			switch (i) {
            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
					 this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature1, this.classSignature1, entryTime, exitTime);
                     break;
            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
					 this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature2, this.classSignature2, entryTime, exitTime);
                     break;
            case 2:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
					 this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature3, this.classSignature3, entryTime, exitTime);
                     break;
            case 3:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
					 this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature4, this.classSignature4, entryTime, exitTime);
                     break;
            case 4:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
					 this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature5, this.classSignature5, entryTime, exitTime);
                     break;
            default: optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
					 this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature1, this.classSignature1, entryTime, exitTime);
            		 break;
			}
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
				entryTime += 2;
				exitTime += 2;
			}
		}
		this.usageModelBuilder.connect(lastAction, stop);
		
//		final Optional<Correspondent> optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
//		if (optionCorrespondent.isPresent()) {
//			final Correspondent correspondent = optionCorrespondent.get();
//			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
//			this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
//			this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
//			lastAction = entryLevelSystemCall;
//			this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature1, this.classSignature1, entryTime, exitTime);
//			entryTime += 2;
//			exitTime += 2;
//		}
//		final Optional<Correspondent> optionCorrespondent2 = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
//		if (optionCorrespondent2.isPresent()) {
//			final Correspondent correspondent = optionCorrespondent2.get();
//			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
//			this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
//			this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
//			lastAction = entryLevelSystemCall;
//			this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature2, this.classSignature2, entryTime, exitTime);
//			entryTime += 2;
//			exitTime += 2;
//		}
//		final Optional<Correspondent> optionCorrespondent3 = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
//		if (optionCorrespondent3.isPresent()) {
//			final Correspondent correspondent = optionCorrespondent3.get();
//			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
//			this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
//			this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
//			lastAction = entryLevelSystemCall;
//			this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature3, this.classSignature3, entryTime, exitTime);
//			entryTime += 2;
//			exitTime += 2;
//		}
//		final Optional<Correspondent> optionCorrespondent4 = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
//		if (optionCorrespondent4.isPresent()) {
//			final Correspondent correspondent = optionCorrespondent4.get();
//			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
//			this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
//			this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
//			lastAction = entryLevelSystemCall;
//			this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature4, this.classSignature4, entryTime, exitTime);
//			entryTime += 2;
//			exitTime += 2;
//		}
//		final Optional<Correspondent> optionCorrespondent5 = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
//		if (optionCorrespondent5.isPresent()) {
//			final Correspondent correspondent = optionCorrespondent5.get();
//			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
//			this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
//			this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
//			lastAction = entryLevelSystemCall;
//			this.addEntryCallEventToUserSessionsEntryCallSequences(entryCallSequenceModel, this.operationSignature5, this.classSignature5, entryTime, exitTime);
//			entryTime += 2;
//			exitTime += 2;
//		}
//		this.usageModelBuilder.connect(lastAction,stop);
		
		saveModel(usageModel,usageModelFolder+"test.usagemodel");
		
		return new TestElements(entryCallSequenceModel, usageModel);
	}
	
	public TestElements getSimpleLoopTestModel(int thinkTime, boolean isClosedWorkload) throws IOException {

		int numberOfConcurrentUsers = this.getRandomInteger(200, 20);
		int numberOfLoops = this.getRandomInteger(5, 2);
		int numberOfIteratedCalls = this.getRandomInteger(5, 1);
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		AbstractUserAction lastAction;
		
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		if(isClosedWorkload)
			this.usageModelBuilder.createClosedWorkload(numberOfConcurrentUsers, thinkTime, usageScenario);
		else 
			this.usageModelBuilder.createOpenWorkload(0, usageScenario);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		
		// usage model behavior
		Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
		this.usageModelBuilder.connect(start, loop);
		final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmLoopIteration.setSpecification(String.valueOf(numberOfLoops));
		loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
		this.usageModelBuilder.connect(loop,stop);
		
		// Loop behavior
		Start loopStart = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
		Stop loopStop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
		lastAction = loopStart;
		Optional<Correspondent> optionCorrespondent;
		for(int i=0;i<numberOfIteratedCalls;i++) {
			switch (i) {
            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
                     break;
            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
                     break;
            case 2:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
                     break;
            case 3:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
                     break;
            case 4:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
                     break;
            default: optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
            		 break;
			}
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
		}
		this.usageModelBuilder.connect(lastAction, loopStop);
		
	
		// Corresponding EntryCallSequenceModel
		int entryTime = 1;
		int exitTime = 2;
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			entryTime = 1;
			exitTime = 2;
			for(int loopCount=0;loopCount<numberOfLoops;loopCount++) {
				for(int j=0;j<numberOfIteratedCalls;j++) {
					EntryCallEvent entryCallEvent = null;
					switch (j) {
		            case 0:  entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
		                     break;
		            case 1:  entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
		                     break;
		            case 2:  entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
		                     break;
		            case 3:  entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
		                     break;
		            case 4:  entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
		                     break;
		            default: entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
		            		 break;
					}
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
				}
			}
		}
		
		saveModel(usageModel,usageModelFolder+"test.usagemodel");
		return new TestElements(entryCallSequenceModel, usageModel);
	}
	
	
	public TestElements getSimpleBranchTestModel(int thinkTime, boolean isClosedWorkload) throws IOException {

		
		int countOfBranchTransitions = this.getRandomInteger(5, 2);
		int numberOfConcurrentUsers = this.getRandomInteger(50*countOfBranchTransitions, 5*countOfBranchTransitions);
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		AbstractUserAction lastAction;
		
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		if(isClosedWorkload)
			this.usageModelBuilder.createClosedWorkload(numberOfConcurrentUsers, thinkTime, usageScenario);
		else 
			this.usageModelBuilder.createOpenWorkload(0, usageScenario);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		
		// usage model behavior
		org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("", scenarioBehaviour);
		this.usageModelBuilder.connect(start, branch);
		this.usageModelBuilder.connect(branch, stop);
		
		// Creates branch transitions according to the random countOfBranchTransitions
		for(int i=0;i<countOfBranchTransitions;i++) {
			BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branch);
			ScenarioBehaviour branchTransitionBehaviour = branchTransition.getBranchedBehaviour_BranchTransition();
			Start startBranchTransition = this.usageModelBuilder.createStart("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
			Stop stopBranchTransition = this.usageModelBuilder.createStop("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
			lastAction = startBranchTransition;
			Optional<Correspondent> optionCorrespondent;
			switch (i) {
            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
                     break;
            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
                     break;
            case 2:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
                     break;
            case 3:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
                     break;
            case 4:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
                     break;
            default: optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
            		 break;
			}
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			this.usageModelBuilder.connect(lastAction, stopBranchTransition);
		}
				
		
		// Corresponding EntryCallSequenceModel
		List<Integer> branchTransitionCounter = new ArrayList<>();
		boolean areAllBranchesVisited = true;
		do {
			for(int i=0;i<branch.getBranchTransitions_Branch().size();i++) {
				branchTransitionCounter.add(i,0);
			}
			for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {	
				int branchDecisioner = this.getRandomInteger(countOfBranchTransitions-1, 0);
				if(branchDecisioner==0) {
					int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
					branchTransitionCounter.set(0, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				} else if(branchDecisioner==1) {
					int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
					branchTransitionCounter.set(1, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				} else if(branchDecisioner==2) {
					int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
					branchTransitionCounter.set(2, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				} else if(branchDecisioner==3) {
					int countOfBranchTransition = branchTransitionCounter.get(3) + 1;
					branchTransitionCounter.set(3, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				} else if(branchDecisioner==4) {
					int countOfBranchTransition = branchTransitionCounter.get(4) + 1;
					branchTransitionCounter.set(4, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				}
			}
			for(int i=0;i<branchTransitionCounter.size();i++) {
				if(branchTransitionCounter.get(i)==0) {
					areAllBranchesVisited = false;
					break;
				}
			}
		} while(!areAllBranchesVisited);
		
		// Set likelihoods of branch transitions		
		for(int i=0;i<branch.getBranchTransitions_Branch().size();i++) {
			branch.getBranchTransitions_Branch().get(i).setBranchProbability((double)branchTransitionCounter.get(i)/(double)numberOfConcurrentUsers);
		}
		
		saveModel(usageModel,usageModelFolder+"test.usagemodel");
		return new TestElements(entryCallSequenceModel, usageModel);
	}
	
	public EntryCallSequenceModel getLoopedBranchTestModel() throws IOException {
		int numberOfLoops = 11;
		int numberOfConcurrentUsers = (int) Math.pow(2, numberOfLoops);
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		this.usageModelBuilder.createClosedWorkload(numberOfConcurrentUsers, 1, usageScenario);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		
		// usage model behavior
		Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
		ScenarioBehaviour loopScenarioBehaviour = loop.getBodyBehaviour_Loop();
		this.usageModelBuilder.connect(start, loop);
		final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmLoopIteration.setSpecification(String.valueOf(numberOfLoops));
		loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
		this.usageModelBuilder.connect(loop,stop);
		 
		/*
		 * Loop behavior start
		 */
		Start loopStart = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(loopScenarioBehaviour, loopStart);
		Stop loopStop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(loopScenarioBehaviour, loopStop);
		
		// EntryCalls
//		EntryLevelSystemCall entryLevelSystemCall = this.usageModelProvider.getEntryLevelSystemCall(this.classSignature1, this.operationSignature1, this.correspondenceModel);
//		this.usageModelBuilder.addUserAction(loopScenarioBehaviour, entryLevelSystemCall);
//		this.usageModelBuilder.connect(loopStart, entryLevelSystemCall);
//		EntryLevelSystemCall entryLevelSystemCall2 = this.usageModelProvider.getEntryLevelSystemCall(this.classSignature2, this.operationSignature2, this.correspondenceModel);
//		this.usageModelBuilder.addUserAction(loopScenarioBehaviour, entryLevelSystemCall2);
//		this.usageModelBuilder.connect(entryLevelSystemCall, entryLevelSystemCall2);
				
		// Branch
		org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("", loopScenarioBehaviour);
		this.usageModelBuilder.connect(loopStart, branch);
		
		// Branch transition1
		BranchTransition branchTransition1 = this.usageModelBuilder.createBranchTransition(branch);
		ScenarioBehaviour branchTransition1Behaviour = branchTransition1.getBranchedBehaviour_BranchTransition();
		Start startBranchTransition1 = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(branchTransition1Behaviour, startBranchTransition1);
		Stop stopBranchTransition1 = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(branchTransition1Behaviour, stopBranchTransition1);
		EntryLevelSystemCall entryLevelSystemCall3 = this.usageModelBuilder.createEntryLevelSystemCall(this.operationSignature3);
		this.usageModelBuilder.addUserAction(branchTransition1Behaviour, entryLevelSystemCall3);
		this.usageModelBuilder.connect(startBranchTransition1, entryLevelSystemCall3);
		this.usageModelBuilder.connect(entryLevelSystemCall3, stopBranchTransition1);
		
		// Branch transition2
		BranchTransition branchTransition2 = this.usageModelBuilder.createBranchTransition(branch);
		ScenarioBehaviour branchTransition2Behaviour = branchTransition2.getBranchedBehaviour_BranchTransition();
		Start startBranchTransition2 = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(branchTransition2Behaviour, startBranchTransition2);
		Stop stopBranchTransition2 = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(branchTransition2Behaviour, stopBranchTransition2);
		EntryLevelSystemCall entryLevelSystemCall4 = this.usageModelBuilder.createEntryLevelSystemCall(this.operationSignature4);
		this.usageModelBuilder.addUserAction(branchTransition2Behaviour, entryLevelSystemCall4);
		this.usageModelBuilder.connect(startBranchTransition2, entryLevelSystemCall4);
		this.usageModelBuilder.connect(entryLevelSystemCall4, stopBranchTransition2);
		
		// Entry Call
		EntryLevelSystemCall entryLevelSystemCall5 = this.usageModelBuilder.createEntryLevelSystemCall(this.operationSignature5);
		this.usageModelBuilder.addUserAction(loopScenarioBehaviour, entryLevelSystemCall5);
		this.usageModelBuilder.connect(branch, entryLevelSystemCall5);
		this.usageModelBuilder.connect(entryLevelSystemCall5, loopStop);
		
		/*
		 * Loop behavior end
		 */
		
		
		// Corresponding EntryCallSequenceModel
		int countOfCallEvent3 = 0;
		int countOfCallEvent4 = 0;
		int entryTime = 1;
		int exitTime = 2;
		boolean branchDecision = false;

		HashMap<Integer,List<List<UserSession>>> userSessionGroups = new HashMap<Integer,List<List<UserSession>>>();
		List<List<UserSession>> startList = new ArrayList<List<UserSession>>();
		startList.add(entryCallSequenceModel.getUserSessions());
		userSessionGroups.put(0, startList);
		
		// Loop
		for(int j=0;j<numberOfLoops;j++) {
			
			countOfCallEvent3 = 0;
			countOfCallEvent4 = 0;
			
			List<List<UserSession>> newUserSessionGroups = new ArrayList<List<UserSession>>();
			
			for(int k=0;k<userSessionGroups.get(j).size();k++) {
				
				for(int i=0;i<2;i++) {
					List<UserSession> userSessions = new ArrayList<UserSession>();
					newUserSessionGroups.add(userSessions);
				}
				int indexGroupCallEvent3 = newUserSessionGroups.size()-2;
				int indexGroupCallEvent4 = newUserSessionGroups.size()-1;
				
				for(int i=0;i<userSessionGroups.get(j).get(k).size();i++) {
				
					if(newUserSessionGroups.get(indexGroupCallEvent3).size()>newUserSessionGroups.get(indexGroupCallEvent4).size())
						branchDecision = false;
					else 
						branchDecision = true;						
					
					// Branch
					if(branchDecision) {
						EntryCallEvent entryCallEvent3 = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
						userSessionGroups.get(j).get(k).get(i).add(entryCallEvent3, true);
						newUserSessionGroups.get(indexGroupCallEvent3).add(userSessionGroups.get(j).get(k).get(i));
						countOfCallEvent3++;
						entryTime += 1;
						exitTime += 1;
					} else {
						EntryCallEvent entryCallEvent4 = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
						userSessionGroups.get(j).get(k).get(i).add(entryCallEvent4, true);
						newUserSessionGroups.get(indexGroupCallEvent4).add(userSessionGroups.get(j).get(k).get(i));
						countOfCallEvent4++;
						entryTime += 1;
						exitTime += 1;
					}
					
					EntryCallEvent entryCallEvent5 = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					userSessionGroups.get(j).get(k).get(i).add(entryCallEvent5, true);
					entryTime += 1;
					exitTime += 1;
					
					entryTime -= 2;
					exitTime -= 2;
				}
						
			}
			
			userSessionGroups.put(j+1, newUserSessionGroups);
			entryTime += 2;
			exitTime += 2;
		}
		
		// Set likelihoods of branch transitions
		double likelihoodOfCallEvent3 = (double)countOfCallEvent3/(double)numberOfConcurrentUsers;
		double likelihoodOfCallEvent4 = (double)countOfCallEvent4/(double)numberOfConcurrentUsers;
		branchTransition1.setBranchProbability(likelihoodOfCallEvent3);
		branchTransition2.setBranchProbability(likelihoodOfCallEvent4);
		
		saveModel(usageModel,usageModelFolder+"LoopedBranchTestUsageModel.usagemodel");
		
		return entryCallSequenceModel;
	}
	
	private void addEntryCallEventToUserSessionsEntryCallSequences(EntryCallSequenceModel entryCallSequenceModel, String operationSignature, String classSignature, long entryTime, long exitTime) {
		EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, operationSignature, classSignature, "sessionId", "hostname");
		for(UserSession userSession : entryCallSequenceModel.getUserSessions()) {
			userSession.add(entryCallEvent);
		}
	}
	
	
	private List<UserSession> getUserSessions (int numberOfUserSessionsToCreate) {
		List<UserSession> userSessions = new ArrayList<UserSession>();
		for(int i=0;i<numberOfUserSessionsToCreate;i++) {
			UserSession userSession = new UserSession("host",String.valueOf(i));
			userSessions.add(userSession);
		}
		return userSessions;
	}
	
	private int getRandomInteger(int max, int min) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	private double getRandomDouble(double max, double min) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}
	
	public final void saveModel(final EObject obj, String saveDestination) throws IOException {

		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);

		final Resource res = resSet.createResource(URI.createFileURI(saveDestination));
		res.getContents().add(obj);
		try {
			res.save(null);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	
	public void writeToCsv(String fileName, List<Object> dataValues, List<Long> timeValues) {
		try
		{
		    if(dataValues.size()!=timeValues.size())
		    	return;
			
		    FileWriter writer = new FileWriter(usageModelFolder+fileName);
		    
		    for(int i=0;i<dataValues.size();i++) {
		    
			    writer.append(String.valueOf(dataValues.get(i)));
			    writer.append(',');
			    writer.append(String.valueOf(timeValues.get(i)));
			    writer.append('\n');
		    
		    }
				
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	}
	

	

}
