package org.iobserve.analysis.userbehavior.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import com.google.common.base.Optional;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.UsageModelBuilder;

/**
 * 
 * It is used to evaluate the approach's modeling accuracy
 * Therefore, reference usage models are created. Each contains a certain user behavior of the research question RQ-1.1 - RQ-1.9
 * According to the user behavior user sessions are created whose call sequences correspond to the user behavior of the reference model
 * Subsequently, the approach can be executed with the created user sessions and the obtained usage model can be matched against the
 * reference usage model for correspondence. The matching procedure is implemented in {@link org.iobserve.analysis.userbehavior.test.UserBehaviorEvaluation}   
 * 
 * @author David
 *
 */
public class ReferenceUsageModelBuilder {
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
	
	
	public ReferenceUsageModelBuilder() {
		this.usageModelBuilder = new UsageModelBuilder(AnalysisMain.getInstance().getModelProviderPlatform().getUsageModelProvider());
		this.correspondenceModel = AnalysisMain.getInstance().getModelProviderPlatform().getCorrespondenceModel();
	}
	
	/**
	 * 
	 * Creates a reference model that contains a loop element. Accordingly, user sessions whose call sequences contain 
	 * iterated calls are created.(RQ-1.3)
	 * 
	 * @return the reference usage model and a corresponding EntryCallSequenceModel
	 * @throws IOException
	 */
	public ReferenceElements getSimpleLoopReferenceModel() throws IOException {

		// Random model parameters
		int numberOfConcurrentUsers = this.getRandomInteger(200, 1);
		int loopCount = this.getRandomInteger(5, 2);
		int numberOfIteratedCalls = this.getRandomInteger(5, 1);
		
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		AbstractUserAction lastAction;
		ReferenceElements referenceElements = new ReferenceElements();
		
		/*
		 * Creation of the reference usage model 
		 */
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);

		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		
		// usage model behavior
		Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
		this.usageModelBuilder.connect(start, loop);
		final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmLoopIteration.setSpecification(String.valueOf(loopCount));
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
		
		/*
		 * Creation of the corresponding user sessions
		 */
		int entryTime = 1;
		int exitTime = 2;
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			entryTime = 1;
			exitTime = 2;
			for(int k=0;k<loopCount;k++) {
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
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
		referenceElements.setUsageModel(usageModel);
		
		return referenceElements;
	}
	
	
	/**
	 * Creates a reference model that contains a branch element. Accordingly, user sessions whose call sequences differ from
	 * each other at the position of the branch are created.(RQ-1.2)
	 * 
	 * @return a reference usage model and corresponding user sessions
	 * @throws IOException
	 */
	public ReferenceElements getSimpleBranchReferenceModel() throws IOException {
		
		// Random model parameters
		int numberOfBranchTransitions = this.getRandomInteger(5, 2);
		int numberOfConcurrentUsers = this.getRandomInteger(200, 10*numberOfBranchTransitions);
		
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		AbstractUserAction lastAction;
		Optional<Correspondent> optionCorrespondent;
		ReferenceElements referenceElements = new ReferenceElements();
		
		/*
		 * Creation of the reference usage model
		 */
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		lastAction = start;
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		
		// usage model behavior
		org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("", scenarioBehaviour);
		this.usageModelBuilder.connect(start, branch);
		
		// Creates branch transitions according to the random number of branch transitions
		for(int i=0;i<numberOfBranchTransitions;i++) {
			BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branch);
			ScenarioBehaviour branchTransitionBehaviour = branchTransition.getBranchedBehaviour_BranchTransition();
			Start startBranchTransition = this.usageModelBuilder.createStart("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
			Stop stopBranchTransition = this.usageModelBuilder.createStop("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
			lastAction = startBranchTransition;
			
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
			if(i==0)
				optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
			else
				optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			
			this.usageModelBuilder.connect(lastAction, stopBranchTransition);
		}
		optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
		if (optionCorrespondent.isPresent()) {
			final Correspondent correspondent = optionCorrespondent.get();
			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
			this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
			this.usageModelBuilder.connect(branch, entryLevelSystemCall);
			lastAction = entryLevelSystemCall;
		}
		
		this.usageModelBuilder.connect(lastAction, stop);
				
		
		/*
		 * Creation of the corresponding user sessions
		 */
		// The branch transition counter ensures that each branch transition is visited by at least one user session
		List<Integer> branchTransitionCounter = new ArrayList<>();
		boolean areAllBranchesVisited = true;
		do {
			for(int i=0;i<branch.getBranchTransitions_Branch().size();i++) {
				branchTransitionCounter.add(i,0);
			}
			for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {	
				int branchDecisioner = this.getRandomInteger(numberOfBranchTransitions-1, 0);
				if(branchDecisioner==0) {
					int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
					branchTransitionCounter.set(0, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
				} else if(branchDecisioner==1) {
					int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
					branchTransitionCounter.set(1, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
				} else if(branchDecisioner==2) {
					int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
					branchTransitionCounter.set(2, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
				} else if(branchDecisioner==3) {
					int countOfBranchTransition = branchTransitionCounter.get(3) + 1;
					branchTransitionCounter.set(3, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
				} else if(branchDecisioner==4) {
					int countOfBranchTransition = branchTransitionCounter.get(4) + 1;
					branchTransitionCounter.set(4, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
				}
				EntryCallEvent entryCallEvent3 = new EntryCallEvent(3, 4, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent3, true);
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
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
		referenceElements.setUsageModel(usageModel);
		
		return referenceElements;
	}
	
	/**
	 * 
	 * Creates a reference model that contains a simple sequence of calls. Accordingly, user sessions whose call sequences contain 
	 * a simple call sequence are created. (RQ-1.1)
	 * It is also used to evaluate the accuracy of workload specifications. Therefore, varying workload is generated by
	 * random entry and exit times of the user sessions, a random number of user sessions for a closed workload specification and
	 * a random mean inter arrival time for an open workload specification (RQ-1.9)
	 * 
	 * @param thinkTime of a closed workload. 
	 * @param isClosedWorkload decides whether a closed or an open workload is created
	 * @return the reference usage model and a corresponding EntryCallSequenceModel
	 * @throws IOException
	 */
	public ReferenceElements getSimpleSequenceReferenceModel(int thinkTime, boolean isClosedWorkload) throws IOException {

		// Random model parameters
		int numberOfUsersSessions = this.getRandomInteger(200,1);
		int numberOfCalls = this.getRandomInteger(5,1);
		
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfUsersSessions));
		AbstractUserAction lastAction;
		ReferenceElements referenceElements = new ReferenceElements();
		
		/*
		 * Creation of the reference model 
		 */
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		lastAction = start;
		Optional<Correspondent> optionCorrespondent;
		for(int i=0;i<numberOfCalls;i++) {
			switch (i) {
			case 0:
            case 5:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
                     break;
            case 1:
            case 6:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
                     break;
            case 2:
            case 7:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
                     break;
            case 3:
            case 8:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
                     break;
            case 4:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
                     break;
            default: optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
            		 break;
			}
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
		}
		this.usageModelBuilder.connect(lastAction, stop);
		
		/*
		 * Creation of the corresponding user sessions
		 */
		int entryTime = 0;
		int exitTime = 1;
		int meanInterArrivalTime = getRandomInteger(30, 1);
		
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			if(isClosedWorkload) {
				entryTime = getRandomInteger(30,1);
				exitTime = entryTime + 1;
			} else {
				entryTime  += meanInterArrivalTime;
				exitTime  += meanInterArrivalTime;
			}
			for(int k=0;k<numberOfCalls;k++) {
				EntryCallEvent entryCallEvent = null;
				switch (k) {
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
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
		referenceElements.setUsageModel(usageModel);
		referenceElements.setMeanInterArrivalTime(meanInterArrivalTime+(numberOfCalls*2));
		referenceElements.setMeanConcurrentUserSessions(this.calculateTheNumberOfConcurrentUsers(entryCallSequenceModel.getUserSessions()));

		return referenceElements;
	}
	
	
	/**
	 * Creates a reference model that contains a loop element. The user sessions contain iterated call sequences
	 * that share overlapping calls. Therefore, it is checked whether the approach transforms the iterated call sequence
	 * that consists of more calls to a loop.(RQ-1.4)
	 * 
	 * @return
	 * @throws IOException
	 */
	public ReferenceElements getOverlappingIterationReferenceModel() throws IOException {
		
		ReferenceElements referenceElements = new ReferenceElements();
		int numberOfConcurrentUsers = this.getRandomInteger(200, 1);
		int loopCount1 = this.getRandomInteger(3, 2);
		int lengthOfSequence1 = 2 * loopCount1;			
		int loopCount2;
		if(loopCount1==3)
			loopCount2 = 2;
		else 
			loopCount2 = 3;
		int lengthOfSequence2 = 2 * loopCount2;
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		AbstractUserAction lastAction;
		
		/*
		 * Creation of the reference usage model
		 */
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		
		// usage model behavior
		Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
		if(lengthOfSequence1>=lengthOfSequence2) {
			this.usageModelBuilder.connect(start, loop);
			final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
			pcmLoopIteration.setSpecification(String.valueOf(loopCount1));
			loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
			// Loop behavior
			Start loopStart = this.usageModelBuilder.createStart("");
			this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
			Stop loopStop = this.usageModelBuilder.createStop("");
			this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
			lastAction = loopStart;
			Optional<Correspondent> optionCorrespondent;
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			this.usageModelBuilder.connect(lastAction, loopStop);
			
			lastAction = loop;
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			
			this.usageModelBuilder.connect(lastAction, stop);
			
		} else {
			
			lastAction = start;
			Optional<Correspondent> optionCorrespondent;
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			
			this.usageModelBuilder.connect(lastAction, loop);
			final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
			pcmLoopIteration.setSpecification(String.valueOf(loopCount2));
			loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
			Start loopStart = this.usageModelBuilder.createStart("");
			this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
			Stop loopStop = this.usageModelBuilder.createStop("");
			this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
			lastAction = loopStart;
			
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
			if (optionCorrespondent.isPresent()) {
				final Correspondent correspondent = optionCorrespondent.get();
				EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
				this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
				lastAction = entryLevelSystemCall;
			}
			
			this.usageModelBuilder.connect(lastAction, loopStop);
			this.usageModelBuilder.connect(loop, stop);
		}
		
		/*
		 * Creation of corresponding user sessions
		 */
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			if(lengthOfSequence1>=lengthOfSequence2) {
				EntryCallEvent entryCallEvent;
				int entryTime = 1;
				int exitTime = 2;
				for(int k=0;k<loopCount1;k++) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				}
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
			} else {
				EntryCallEvent entryCallEvent;
				int entryTime = 1;
				int exitTime = 2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				for(int k=0;k<loopCount2;k++) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				}
			}
		}
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
		referenceElements.setUsageModel(usageModel);
		
		return referenceElements;
	}
	
	
	/**
	 * It creates a reference usage model that contains nested branches. Accordingly, user sessions whose call sequences differ 
	 * from each other at the positions of the branches are created.(RQ-1.5) 
	 * 
	 * @return a reference usage model and corresponding user sessions
	 * @throws IOException
	 */
	public ReferenceElements getBranchWithinBranchReferenceModel() throws IOException {
		
		int numberOfTransitionsOfExteriorBranch = this.getRandomInteger(3, 2);
		int numberOfTransitionsOfInteriorBranches = this.getRandomInteger(3, 2);
		int numberOfConcurrentUsers = this.getRandomInteger(200, 10*numberOfTransitionsOfExteriorBranch);
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		ReferenceElements referenceElements = new ReferenceElements();
		
		AbstractUserAction lastAction;
		Optional<Correspondent> optionCorrespondent;
		
		/*
		 * Creation of user sessions
		 */
		List<Integer> branchTransitionCounter = new ArrayList<>();
		List<List<Integer>> listOfbranchTransitionCounterInterior = new ArrayList<>();
		boolean areAllBranchesVisited = true;
		do {
			for(int i=0;i<numberOfTransitionsOfExteriorBranch;i++) {
				branchTransitionCounter.add(i,0);
				List<Integer> branchTransitionCounterInterior = new ArrayList<>();
				for(int j=0;j<numberOfTransitionsOfInteriorBranches;j++) {
					branchTransitionCounterInterior.add(j,0);
				}
				listOfbranchTransitionCounterInterior.add(i,branchTransitionCounterInterior);
			}
			int entryTime = 1;
			int exitTime = 2;
			
			for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
				entryTime = 1;
				exitTime = 2;
				
				int branchDecisioner = this.getRandomInteger(numberOfTransitionsOfExteriorBranch-1, 0);
				if(branchDecisioner==0) {
					int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
					branchTransitionCounter.set(0, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
					branchDecisioner = this.getRandomInteger(numberOfTransitionsOfInteriorBranches-1, 0);
					for(int k=0;k<numberOfTransitionsOfInteriorBranches;k++) {
						if(listOfbranchTransitionCounterInterior.get(0).get(k)==0) {
							branchDecisioner=k;
							break;
						}
					}
					if(branchDecisioner==0) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(0).get(0) + 1;
						listOfbranchTransitionCounterInterior.get(0).set(0, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;	
					} else if(branchDecisioner==1) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(0).get(1) + 1;
						listOfbranchTransitionCounterInterior.get(0).set(1, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					} else if(branchDecisioner==2) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(0).get(2) + 1;
						listOfbranchTransitionCounterInterior.get(0).set(2, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					} 	
				} else if(branchDecisioner==1) {
					int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
					branchTransitionCounter.set(1, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
					branchDecisioner = this.getRandomInteger(numberOfTransitionsOfInteriorBranches-1, 0);
					for(int k=0;k<numberOfTransitionsOfInteriorBranches;k++) {
						if(listOfbranchTransitionCounterInterior.get(1).get(k)==0) {
							branchDecisioner=k;
							break;
						}
					}
					if(branchDecisioner==0) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(1).get(0) + 1;
						listOfbranchTransitionCounterInterior.get(1).set(0, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
						
					} else if(branchDecisioner==1) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(1).get(1) + 1;
						listOfbranchTransitionCounterInterior.get(1).set(1, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					} else if(branchDecisioner==2) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(1).get(2) + 1;
						listOfbranchTransitionCounterInterior.get(1).set(2, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					} 
				} else if(branchDecisioner==2) {
					int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
					branchTransitionCounter.set(2, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
					branchDecisioner = this.getRandomInteger(numberOfTransitionsOfInteriorBranches-1, 0);
					for(int k=0;k<numberOfTransitionsOfInteriorBranches;k++) {
						if(listOfbranchTransitionCounterInterior.get(2).get(k)==0) {
							branchDecisioner=k;
							break;
						}
					}
					if(branchDecisioner==0) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(2).get(0) + 1;
						listOfbranchTransitionCounterInterior.get(2).set(0, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
						
					} else if(branchDecisioner==1) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(2).get(1) + 1;
						listOfbranchTransitionCounterInterior.get(2).set(1, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					} else if(branchDecisioner==2) {
						countOfBranchTransition = listOfbranchTransitionCounterInterior.get(2).get(2) + 1;
						listOfbranchTransitionCounterInterior.get(2).set(2, countOfBranchTransition);
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					} 
				} 
			}
			for(int i=0;i<branchTransitionCounter.size();i++) {
				if(branchTransitionCounter.get(i)==0) {
					areAllBranchesVisited = false;
					break;
				}
			}
			for(List<Integer> branchTransitionCounterInterior : listOfbranchTransitionCounterInterior) {
				for(int j=0;j<branchTransitionCounterInterior.size();j++) {
					if(branchTransitionCounterInterior.get(j)==0) {
						areAllBranchesVisited = false;
						break;
					}
				}
				if(!areAllBranchesVisited)
					break;
			}
			if(!areAllBranchesVisited) {
				listOfbranchTransitionCounterInterior.clear();
				branchTransitionCounter.clear();
			}
		} while(!areAllBranchesVisited);
		
		
		/*
		 * Creation of the reference usage model
		 */
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		lastAction = start;
	
		// Branch1
		org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("", scenarioBehaviour);
		this.usageModelBuilder.connect(lastAction, branch);
		this.usageModelBuilder.connect(branch, stop);
		
		// Creates branch transitions according to the random countOfBranchTransitions
		for(int i=0;i<numberOfTransitionsOfExteriorBranch;i++) {
			BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branch);
			ScenarioBehaviour branchTransitionBehaviour = branchTransition.getBranchedBehaviour_BranchTransition();
			branchTransition.setBranchProbability((double)branchTransitionCounter.get(i)/(double)numberOfConcurrentUsers);
			Start startBranchTransition = this.usageModelBuilder.createStart("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
			Stop stopBranchTransition = this.usageModelBuilder.createStop("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
			lastAction = startBranchTransition;
			switch (i) {
            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
                     break;
            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
                     break;
            case 2:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
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

			// Interior Branch
			org.palladiosimulator.pcm.usagemodel.Branch branchInterior = this.usageModelBuilder.createBranch("", branchTransitionBehaviour);
			this.usageModelBuilder.connect(lastAction, branchInterior);
			this.usageModelBuilder.connect(branchInterior, stopBranchTransition);
			
			for(int j=0;j<numberOfTransitionsOfInteriorBranches;j++) {
				BranchTransition branchTransitionInterior = this.usageModelBuilder.createBranchTransition(branchInterior);
				ScenarioBehaviour branchTransitionBehaviourInterior = branchTransitionInterior.getBranchedBehaviour_BranchTransition();
				branchTransitionInterior.setBranchProbability((double)listOfbranchTransitionCounterInterior.get(i).get(j)/(double)branchTransitionCounter.get(i));
				Start startBranchTransitionInterior = this.usageModelBuilder.createStart("");
				this.usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, startBranchTransitionInterior);
				Stop stopBranchTransitionInterior = this.usageModelBuilder.createStop("");
				this.usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, stopBranchTransitionInterior);
				lastAction = startBranchTransitionInterior;
				switch (j) {
	            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
	                     break;
	            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
	                     break;
	            case 2:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
	                     break;
	            default: optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
	            		 break;
				}
				if (optionCorrespondent.isPresent()) {
					final Correspondent correspondent = optionCorrespondent.get();
					EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
					this.usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, entryLevelSystemCall);
					this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
					lastAction = entryLevelSystemCall;
				}
				this.usageModelBuilder.connect(lastAction, stopBranchTransitionInterior);	
				
			}
			
		}
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
		referenceElements.setUsageModel(usageModel);
		
		return referenceElements;
	}
	
	
	/**
	 * It creates a reference usage model that contains loops within branches. Accordingly, user sessions whose call sequences differ 
	 * from each other at the positions of the branches and that contain iterated call sequences are created.(RQ-1.6) 
	 * 
	 * @return a reference model and corresponding user sessions
	 * @throws IOException
	 */
	public ReferenceElements getLoopWithinBranchReferenceModel() throws IOException {
		
		int numberOfBranchTransitions = this.getRandomInteger(3, 2);
		int numberOfConcurrentUsers = this.getRandomInteger(30, 10*numberOfBranchTransitions);
		int lengthOfBranchSequence = this.getRandomInteger(2, 1);
		int countOfLoop = this.getRandomInteger(3, 2);
		
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		ReferenceElements referenceElements = new ReferenceElements();


		/*
		 * Creation of the reference model
		 */
		AbstractUserAction lastAction;
		Optional<Correspondent> optionCorrespondent;
		
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		lastAction = start;
		
		// usage model behavior
		org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("", scenarioBehaviour);
		this.usageModelBuilder.connect(lastAction, branch);
		this.usageModelBuilder.connect(branch, stop);
		
		// Creates branch transitions according to the random countOfBranchTransitions
		for(int i=0;i<numberOfBranchTransitions;i++) {
			BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branch);
			ScenarioBehaviour branchTransitionBehaviour = branchTransition.getBranchedBehaviour_BranchTransition();
			Start startBranchTransition = this.usageModelBuilder.createStart("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
			Stop stopBranchTransition = this.usageModelBuilder.createStop("");
			this.usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
			lastAction = startBranchTransition;
			switch (i) {
            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
                     break;
            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
                     break;
            case 2:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
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
			if(lengthOfBranchSequence==2) {
				optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
				if (optionCorrespondent.isPresent()) {
					final Correspondent correspondent = optionCorrespondent.get();
					EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
					this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
					this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
					lastAction = entryLevelSystemCall;
				}
			}
			
			Loop loop = this.usageModelBuilder.createLoop("", branchTransitionBehaviour);
			this.usageModelBuilder.connect(lastAction, loop);
			final PCMRandomVariable pcmLoop2Iteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
			pcmLoop2Iteration.setSpecification(String.valueOf(countOfLoop));
			loop.setLoopIteration_Loop(pcmLoop2Iteration); 
			Start loopStart = this.usageModelBuilder.createStart("");
			this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
			Stop loopStop = this.usageModelBuilder.createStop("");
			this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
			lastAction = loopStart;
			
			switch (i) {
            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2, this.operationSignature2);
                     break;
            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
                     break;
            case 2:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1, this.operationSignature1);
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
			this.usageModelBuilder.connect(lastAction, loopStop);
			
			this.usageModelBuilder.connect(loop, stopBranchTransition);
		}
				
		
		/*
		 * Creation of corresponding user sessions
		 */
		List<Integer> branchTransitionCounter = new ArrayList<>();
		boolean areAllBranchesVisited = true;
		do {
			for(int i=0;i<branch.getBranchTransitions_Branch().size();i++) {
				branchTransitionCounter.add(i,0);
			}
			int entryTime = 1;
			int exitTime = 2;
			for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {	
				entryTime = 1;
				exitTime = 2;
				// branch
				int branchDecisioner = this.getRandomInteger(numberOfBranchTransitions-1, 0);
				if(branchDecisioner==0) {
					int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
					branchTransitionCounter.set(0, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
					if(lengthOfBranchSequence==2) {
						EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					}
					// loop
					for(int j=0;j<countOfLoop;j++) {
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					}
				} else if(branchDecisioner==1) {
					int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
					branchTransitionCounter.set(1, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
					if(lengthOfBranchSequence==2) {
						EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					}
					// loop
					for(int j=0;j<countOfLoop;j++) {
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					}
				} else if(branchDecisioner==2) {
					int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
					branchTransitionCounter.set(2, countOfBranchTransition);
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
					if(lengthOfBranchSequence==2) {
						EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					}
					// loop
					for(int j=0;j<countOfLoop;j++) {
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime = entryTime + 2;
						exitTime = exitTime + 2;
					}
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
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
		referenceElements.setUsageModel(usageModel);
		
		return referenceElements;
	}
	
	/**
	 * It creates a reference usage model that contains loops within loops. Accordingly, user sessions whose call sequences 
	 * contain iterated segments that again contain iterated segments are created.(RQ-1.7)
	 * 
	 * @return reference usage model and corresponding user sessions
	 * @throws IOException
	 */
	public ReferenceElements getLoopWithinLoopReferenceModel() throws IOException {

		int numberOfConcurrentUsers = this.getRandomInteger(200, 1);
		int countOfLoop1 = this.getRandomInteger(4, 2);
		int countOfLoop2 = this.getRandomInteger(4, 2);
		int lengthOfSubsequentLoopSequence = this.getRandomInteger(2, 1);
		
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		ReferenceElements referenceElements = new ReferenceElements();

		/*
		 * Creation of the reference usage model
		 */
		Optional<Correspondent> optionCorrespondent;
		AbstractUserAction lastAction;
		
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		lastAction = start;
				
		// loop1
		Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
		this.usageModelBuilder.connect(lastAction, loop);
		this.usageModelBuilder.connect(loop, stop);
		final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmLoopIteration.setSpecification(String.valueOf(countOfLoop1));
		loop.setLoopIteration_Loop(pcmLoopIteration); 
		Start loopStart = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
		Stop loopStop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
		lastAction = loopStart;
		
		// loop2
		Loop loop2 = this.usageModelBuilder.createLoop("", loop.getBodyBehaviour_Loop());
		this.usageModelBuilder.connect(lastAction, loop2);
		final PCMRandomVariable pcmLoop2Iteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmLoop2Iteration.setSpecification(String.valueOf(countOfLoop2));
		loop2.setLoopIteration_Loop(pcmLoop2Iteration); 
		Start loop2Start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(loop2.getBodyBehaviour_Loop(), loop2Start);
		Stop loop2Stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(loop2.getBodyBehaviour_Loop(), loop2Stop);
		lastAction = loop2Start;
		optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
		if (optionCorrespondent.isPresent()) {
			final Correspondent correspondent = optionCorrespondent.get();
			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
			this.usageModelBuilder.addUserAction(loop2.getBodyBehaviour_Loop(), entryLevelSystemCall);
			this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
			lastAction = entryLevelSystemCall;
		}
		this.usageModelBuilder.connect(lastAction, loop2Stop);
		lastAction = loop2;
		
		//loop1 sequel
		// Subsequent loop sequence
		for(int i=0;i<lengthOfSubsequentLoopSequence;i++) {
			switch (i) {
            case 0:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
                     break;
            case 1:  optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
                     break;
            default: optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
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
	
		
		/*
		 * Creation of corresponding user sessions
		 */
		int entryTime = 1;
		int exitTime = 2;
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			entryTime = 1;
			exitTime = 2;
			// loop1
			for(int k=0;k<countOfLoop1;k++) {
				// loop2
				for(int j=0;j<countOfLoop2;j++) {
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
				}
				// Subsequent call sequence of loop1
				for(int j=0;j<lengthOfSubsequentLoopSequence;j++) {
					EntryCallEvent entryCallEvent = null;
					switch (j) {
		            case 0:  entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
		                     break;
		            case 1:  entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
		                     break;
		            default: entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
		            		 break;
					}
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime = entryTime + 2;
					exitTime = exitTime + 2;
				}
			}
		}
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
		referenceElements.setUsageModel(usageModel);
		
		return referenceElements;
	}
	
	
	/**
	 * It creates a reference usage model that contains branches within loops. Accordingly, user sessions whose call sequences differ 
	 * from each other are iterated in a row. Thereby, at each iteration of a difference between the call sequences the probabilities
	 * have to be equal because otherwise it would not be an iteration (RQ-1.8)
	 * 
	 * @return reference usage model and corresponding user sessions
	 * @throws IOException
	 */
	public ReferenceElements getBranchWithinLoopReferenceModel() throws IOException {
		
		int numberOfLoops = this.getRandomInteger(3, 3);
		int numberOfConcurrentUsers = (int) Math.pow(2, numberOfLoops)*5;
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfConcurrentUsers));
		ReferenceElements testElements = new ReferenceElements();
		
		/*
		 * Creation of the reference usage model
		 */
		AbstractUserAction lastAction;
		Optional<Correspondent> optionCorrespondent;
		
		// Usage model basics
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);

		ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		lastAction = start;
		
		// usage model behavior
		Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
		ScenarioBehaviour loopScenarioBehaviour = loop.getBodyBehaviour_Loop();
		this.usageModelBuilder.connect(lastAction, loop);
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
		optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
		if (optionCorrespondent.isPresent()) {
			final Correspondent correspondent = optionCorrespondent.get();
			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
			this.usageModelBuilder.addUserAction(branchTransition1Behaviour, entryLevelSystemCall);
			this.usageModelBuilder.connect(startBranchTransition1, entryLevelSystemCall);
			this.usageModelBuilder.connect(entryLevelSystemCall, stopBranchTransition1);
		}
		
		// Branch transition2
		BranchTransition branchTransition2 = this.usageModelBuilder.createBranchTransition(branch);
		ScenarioBehaviour branchTransition2Behaviour = branchTransition2.getBranchedBehaviour_BranchTransition();
		Start startBranchTransition2 = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(branchTransition2Behaviour, startBranchTransition2);
		Stop stopBranchTransition2 = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(branchTransition2Behaviour, stopBranchTransition2);
		optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
		if (optionCorrespondent.isPresent()) {
			final Correspondent correspondent = optionCorrespondent.get();
			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
			this.usageModelBuilder.addUserAction(branchTransition2Behaviour, entryLevelSystemCall);
			this.usageModelBuilder.connect(startBranchTransition2, entryLevelSystemCall);
			this.usageModelBuilder.connect(entryLevelSystemCall, stopBranchTransition2);
		}
		
		optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
		if (optionCorrespondent.isPresent()) {
			final Correspondent correspondent = optionCorrespondent.get();
			EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
			this.usageModelBuilder.addUserAction(loopScenarioBehaviour, entryLevelSystemCall);
			this.usageModelBuilder.connect(branch, entryLevelSystemCall);
			this.usageModelBuilder.connect(entryLevelSystemCall, loopStop);
		}
		
		
		/*
		 * Creation of corresponding user sessions
		 */
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
						entryTime += 2;
						exitTime += 2;
					} else {
						EntryCallEvent entryCallEvent4 = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
						userSessionGroups.get(j).get(k).get(i).add(entryCallEvent4, true);
						newUserSessionGroups.get(indexGroupCallEvent4).add(userSessionGroups.get(j).get(k).get(i));
						countOfCallEvent4++;
						entryTime += 2;
						exitTime += 2;
					}
					
					EntryCallEvent entryCallEvent5 = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					userSessionGroups.get(j).get(k).get(i).add(entryCallEvent5, true);
					entryTime += 2;
					exitTime += 2;
					
					entryTime -= 4;
					exitTime -= 4;
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
		
		saveModel(usageModel,usageModelFolder+"ReferenceModel.usagemodel");
		testElements.setEntryCallSequenceModel(entryCallSequenceModel);
		testElements.setUsageModel(usageModel);
		
		return testElements;
	}
	
	
	/**
	 * It creates the passed number of user sessions. Thereby, two user groups are distinguished.
	 * Each user session of a user group contains the same call sequence. The call sequences between the
	 * user groups differ from each other by their operation signatures
	 * It is used to evaluate the approach's response times with an increasing number of user sessions (RQ-3.1)
	 * It returns user session that are used to execute the approach and to measure the response time
	 * Thereby, this method is called repeatedly to constantly increase the number of UserSessions 
	 * 
	 * 
	 * @param numberOfUserSessions defines the number of user sessions to create
	 * @return user sessions with a fixed user behavior
	 */
	public ReferenceElements getIncreasingUserSessionsScalabilityReferenceModel(int numberOfUserSessions) {

		ReferenceElements testElements = new ReferenceElements();
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfUserSessions));
		
		int entryTime = 1;
		int exitTime = 2;
		
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			
			double userGroupDecisioner = (double)i/(double)numberOfUserSessions;
			
			// 
			if(userGroupDecisioner<0.3) {
				EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				int branchDecisioner = this.getRandomInteger(2, 1);
				if(branchDecisioner==1) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				} else {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				}
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
			} else {
				EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
				int branchDecisioner = this.getRandomInteger(2, 1);
				if(branchDecisioner==1) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				} else {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				}
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
				entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
				entryTime+=2;
				exitTime+=2;
			}			
			
		}		
		
		testElements.setEntryCallSequenceModel(entryCallSequenceModel);
		return testElements;
	}
		
	/**
	 * It creates a fixed number of user sessions. The number of calls per user session is determined by the passed
	 * numberOfIterations parameter. It defines how often a fixed call sequence is added to the call sequence of each user session
	 * It is used to evaluate the approach's response times with an increasing number of calls
	 * It returns user session that are used to execute the approach and to measure the response time
	 * Thereby, this method is called repeatedly to constantly increase the number of calls per UserSession
	 * 
	 * @param numberOfIterations defines how many calls per user session are created
	 * @return user sessions 
	 */
	public ReferenceElements getIncreasingCallSequenceScalabilityReferenceModel(int numberOfIterations) {
		
		ReferenceElements testElements = new ReferenceElements();
		int numberOfUserSessions = 50;
		EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(this.getUserSessions(numberOfUserSessions));
		
		boolean branchDecisionerUserGroup1 = true;
		boolean branchDecisionerUserGroup2 = true;
		
		for(int i=0;i<entryCallSequenceModel.getUserSessions().size();i++) {
			
			double userGroupDecisioner = (double)i/(double)numberOfUserSessions;
			int entryTime = 1;
			int exitTime = 2;
			
			if(userGroupDecisioner<0.3) {
				branchDecisionerUserGroup1 = !branchDecisionerUserGroup1;
				for(int j=0;j<numberOfIterations;j++) {
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					int branchDecisioner = this.getRandomInteger(2, 1);
					if(branchDecisioner==1) {
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime+=2;
						exitTime+=2;
					} else {
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime+=2;
						exitTime+=2;
					}
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				}
			} else {
				branchDecisionerUserGroup2 = !branchDecisionerUserGroup2;
				for(int j=0;j<numberOfIterations;j++) {
					EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
					int branchDecisioner = this.getRandomInteger(2, 1);
					if(branchDecisioner==1) {
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime+=2;
						exitTime+=2;
					} else {
						entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
						entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
						entryTime+=2;
						exitTime+=2;
					}
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1, String.valueOf(i), "hostname");
					entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
					entryTime+=2;
					exitTime+=2;
				}
			}
						
		}		
		
		testElements.setEntryCallSequenceModel(entryCallSequenceModel);
		return testElements;
	}
	
		
	/**
	 * Creates new user sessions
	 * 
	 * @param numberOfUserSessionsToCreate defines the number of user sessions
	 * @return new user sessions
	 */
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
		
	/**
	 * Calculates the exact mean number of concurrent user sessions as a reference workload
	 * @param sessions used to calculate the mean number of user sessions from
	 * @return mean number of concurrent user sessions
	 */
	private int calculateTheNumberOfConcurrentUsers(final List<UserSession> sessions) {
		int averageNumberOfConcurrentUsers = 0;
		if(sessions.size() > 0) {
			int countOfConcurrentUsers = 0;
			Collections.sort(sessions, this.SortUserSessionByEntryTime);
			for (int i = 0; i < sessions.size(); i++) {
				final long entryTimeUS1 =  getEntryTime(sessions.get(i).getEvents());
				final long exitTimeUS1 = sessions.get(i).getExitTime();
				int numberOfConcurrentUserSessionsDuringThisSession = 1;
				for (int j = 0; j < sessions.size(); j++) {
					if(j==i)
						continue;
					final long entryTimeUS2 =  sessions.get(j).getEntryTime();
					final long exitTimeUS2 = sessions.get(j).getExitTime();
					if(exitTimeUS2<entryTimeUS1)
						continue;
					if(exitTimeUS1>=entryTimeUS2)	
						numberOfConcurrentUserSessionsDuringThisSession++;
				}
				
				countOfConcurrentUsers += numberOfConcurrentUserSessionsDuringThisSession;
			}
			averageNumberOfConcurrentUsers = countOfConcurrentUsers/sessions.size();
		}
		
		return averageNumberOfConcurrentUsers;
	}
		
		
	/**
	 * Utility methods: Comparators, csv writer, model saver
	 */
	
	private final Comparator<UserSession> SortUserSessionByEntryTime = new Comparator<UserSession>() {
		
		@Override
		public int compare(final UserSession o1, final UserSession o2) {
			long entryO1 = getEntryTime(o1.getEvents());
			long entryO2 = getEntryTime(o2.getEvents());
			if(entryO1 > entryO2) {
				return 1;
			} else if(entryO1 < entryO2) {
				return -1;
			}
			return 0;
		}
	};
	
	public long getEntryTime(List<EntryCallEvent> events) {
		long entryTime = 0;
		if (events.size() > 0) {
			this.sortEventsBy(SortEntryCallEventsByEntryTime, events);
			// Here was the bug: First element has to be returned instead of last
			entryTime = events.get(0).getEntryTime();
		}
		return entryTime;
	}
	
	public void sortEventsBy(final Comparator<EntryCallEvent> cmp, List<EntryCallEvent> events) {
		Collections.sort(events,cmp);
	}
	
	public static final Comparator<EntryCallEvent> SortEntryCallEventsByEntryTime = 
			new Comparator<EntryCallEvent>() {
		
		@Override
		public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
			if (o1.getEntryTime() > o2.getEntryTime()) {
				return 1;
			} else if (o1.getEntryTime() < o2.getEntryTime()) {
				return -1;
			}
			return 0;
		}
	};
	
	public void saveModel(final EObject obj, String saveDestination) throws IOException {

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
	
	public void writeAccuracyResults(List<AccuracyResults> accuracyResults) throws IOException {
		
		FileWriter writer = new FileWriter("/Users/David/Desktop/AccuracyEvaluationResults");
		writer.append("jc,srcc");
		writer.append('\n');
		
		for(AccuracyResults accuracyResult : accuracyResults) { 
			writer.append(String.valueOf(accuracyResult.getJc()));
			writer.append(',');
			writer.append(String.valueOf(accuracyResult.getSrcc()));
    		writer.append('\n');
    	}
		
		writer.flush();
	    writer.close();
	}
	
	public void writeRME(List<Double> accuracyResults) throws IOException {
		
		FileWriter writer = new FileWriter("/Users/David/Desktop/RMEResults");
		writer.append("rme");
		writer.append('\n');
		
		double avg = 0;
		
		for(Double rme : accuracyResults) { 
			writer.append(String.valueOf(rme));
    		writer.append('\n');
    		avg += rme;
    	}
		
		avg = avg/accuracyResults.size();
		writer.append(String.valueOf(avg));
		writer.append('\n');
		
		writer.flush();
	    writer.close();
	}

}
