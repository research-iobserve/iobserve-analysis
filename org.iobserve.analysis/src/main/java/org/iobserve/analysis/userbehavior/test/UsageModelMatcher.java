package org.iobserve.analysis.userbehavior.test;

import java.util.ArrayList;
import java.util.List;

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
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
import org.palladiosimulator.pcm.usagemodel.impl.BranchImpl;
import org.palladiosimulator.pcm.usagemodel.impl.ClosedWorkloadImpl;
import org.palladiosimulator.pcm.usagemodel.impl.EntryLevelSystemCallImpl;
import org.palladiosimulator.pcm.usagemodel.impl.LoopImpl;
import org.palladiosimulator.pcm.usagemodel.impl.OpenWorkloadImpl;
import org.palladiosimulator.pcm.usagemodel.impl.StartImpl;
import org.palladiosimulator.pcm.usagemodel.impl.StopImpl;

public class UsageModelMatcher {
	
	public boolean matchUsageModels(final UsageModel usageModel1, final UsageModel usageModel2) {
		
		if(usageModel1==null||usageModel2==null)
			return false;
		if(usageModel1.getUsageScenario_UsageModel().size()!=usageModel2.getUsageScenario_UsageModel().size())
			return false;
		for(int i=0;i<usageModel1.getUsageScenario_UsageModel().size();i++) {
			UsageScenario usageScenarioOfUsageModel1 = usageModel1.getUsageScenario_UsageModel().get(i);
			UsageScenario usageScenarioOfUsageModel2 = usageModel2.getUsageScenario_UsageModel().get(i);
			if(!matchWorkload(usageScenarioOfUsageModel1.getWorkload_UsageScenario(), usageScenarioOfUsageModel2.getWorkload_UsageScenario()))
				return false;
			ScenarioBehaviour scenarioBehaviourOfUsageModel1 = usageScenarioOfUsageModel1.getScenarioBehaviour_UsageScenario();
			ScenarioBehaviour scenarioBehaviourOfUsageModel2 = usageScenarioOfUsageModel2.getScenarioBehaviour_UsageScenario();
			if(!matchScenarioBehavior(scenarioBehaviourOfUsageModel1, scenarioBehaviourOfUsageModel2))
				return false;
		}
		
		return true;
	}
	
	private boolean matchScenarioBehavior(ScenarioBehaviour scenarioBehaviour1, ScenarioBehaviour scenarioBehaviour2) {
		
		List<AbstractUserAction> actionsOfScenarioBehaviour1 = scenarioBehaviour1.getActions_ScenarioBehaviour();
		List<AbstractUserAction> actionsOfScenarioBehaviour2 = scenarioBehaviour2.getActions_ScenarioBehaviour();
		if(actionsOfScenarioBehaviour1.size()!=actionsOfScenarioBehaviour2.size())
			return false;
		AbstractUserAction nextActionOfScenarioBehaviour1 = null;
		AbstractUserAction nextActionOfScenarioBehaviour2 = null;
		for(int i=0;i<actionsOfScenarioBehaviour1.size();i++) {
			if(actionsOfScenarioBehaviour1.get(i).getClass().equals(StartImpl.class))
				nextActionOfScenarioBehaviour1 = actionsOfScenarioBehaviour1.get(i);
			if(actionsOfScenarioBehaviour2.get(i).getClass().equals(StartImpl.class))
				nextActionOfScenarioBehaviour2 = actionsOfScenarioBehaviour2.get(i);
			if(nextActionOfScenarioBehaviour1!=null&&nextActionOfScenarioBehaviour2!=null)
				break;
		}
		if(nextActionOfScenarioBehaviour1==null||nextActionOfScenarioBehaviour2==null)
			return false;
			
		while(true) {
			
			if(nextActionOfScenarioBehaviour1==null&&nextActionOfScenarioBehaviour2==null)
				break;
			if(nextActionOfScenarioBehaviour1.getClass().equals(StopImpl.class)&&nextActionOfScenarioBehaviour2.getClass().equals(StopImpl.class))
				break;
			
			if(!matchAction(nextActionOfScenarioBehaviour1, nextActionOfScenarioBehaviour2))
				return false;
			
			nextActionOfScenarioBehaviour1 = nextActionOfScenarioBehaviour1.getSuccessor();
			nextActionOfScenarioBehaviour2 = nextActionOfScenarioBehaviour2.getSuccessor();
		}
		
		return true;
	}

	/**
	 * TODO from here
	 * @param workload1
	 * @param workload2
	 * @return
	 */
	private boolean matchAction(AbstractUserAction action1, AbstractUserAction action2) {
				
		if(action1==null||action2==null)
			return false;
		if(!(action1.getClass().equals(action2.getClass())))
			return false;
		if(action1.getClass().equals(StartImpl.class))
			return true;
		if(action1.getClass().equals(StopImpl.class))
			return true;
		if(action1.getClass().equals(EntryLevelSystemCallImpl.class)) {
			if(matchEntryLevelSystemCall((EntryLevelSystemCall)action1, (EntryLevelSystemCall)action2))
				return true;
		}
		if(action1.getClass().equals(LoopImpl.class)) {
			if(matchLoop((Loop)action1, (Loop)action2))
				return true;
		}
		if(action1.getClass().equals(BranchImpl.class)) {
			if(matchBranch((Branch)action1, (Branch)action2))
				return true;
		}
		
		return false;
	}

	private boolean matchBranch(Branch branch1, Branch branch2) {
		if(branch1.getBranchTransitions_Branch().size()!=branch2.getBranchTransitions_Branch().size())
			return false;
		List<Integer>matchedTransitions = new ArrayList<Integer>();
		for(int i=0;i<branch1.getBranchTransitions_Branch().size();i++) {
			boolean isMatchFound = false;
			for(int j=0;j<branch2.getBranchTransitions_Branch().size();j++) {
				if(matchedTransitions.contains(j))
					continue;
				if(branch1.getBranchTransitions_Branch().get(i).getBranchProbability()!=branch2.getBranchTransitions_Branch().get(j).getBranchProbability())
					continue;
				if(matchScenarioBehavior(branch1.getBranchTransitions_Branch().get(i).getBranchedBehaviour_BranchTransition(),branch2.getBranchTransitions_Branch().get(j).getBranchedBehaviour_BranchTransition())) {
					isMatchFound = true;
					matchedTransitions.add(j);
					break;
				}
			}
			if(!isMatchFound)
				return false;
		}
			
		return true;
	}

	private boolean matchLoop(Loop loop1, Loop loop2) {
		if(!loop1.getLoopIteration_Loop().getSpecification().equals(loop2.getLoopIteration_Loop().getSpecification()))
			return false;
		if(!matchScenarioBehavior(loop1.getBodyBehaviour_Loop(),loop2.getBodyBehaviour_Loop()))
			return false;
		
		return true;
	}

	private boolean matchEntryLevelSystemCall(EntryLevelSystemCall action1, EntryLevelSystemCall action2) {
		if(!action1.getEntityName().equals(action2.getEntityName()))
			return false;
		return true;
	}

	private boolean matchWorkload(Workload workload1, Workload workload2) {
		if(!(workload1.getClass().equals(workload2.getClass())))
			return false;
		if(workload1.getClass().equals(ClosedWorkloadImpl.class)) {
			ClosedWorkload closedWorkloadOfUsageModel1 = (ClosedWorkload) workload1;
			ClosedWorkload closedWorkloadOfUsageModel2 = (ClosedWorkload) workload2;
			if(closedWorkloadOfUsageModel1.getPopulation()!=closedWorkloadOfUsageModel2.getPopulation())
				return false;
			if(!closedWorkloadOfUsageModel1.getThinkTime_ClosedWorkload().getSpecification().equals(closedWorkloadOfUsageModel2.getThinkTime_ClosedWorkload().getSpecification()))
				return false;
		} else if(workload1.getClass().equals(OpenWorkloadImpl.class)) {
			OpenWorkload openWorkloadOfUsageModel1 = (OpenWorkload) workload1;
			OpenWorkload openWorkloadOfUsageModel2 = (OpenWorkload) workload2;
			if(!openWorkloadOfUsageModel1.getInterArrivalTime_OpenWorkload().getSpecification().equals(openWorkloadOfUsageModel2.getInterArrivalTime_OpenWorkload().getSpecification()))
				return false;
		}
		
		return true;
	}
	
//	private UsageModel getUsageModelInstance(final URI uriInstance) {
//		// Initialize the model
//		UsagemodelPackage.eINSTANCE.eClass();
//
//		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
//		final Map<String, Object> map = reg.getExtensionToFactoryMap();
//		map.put("*", new XMIResourceFactoryImpl());
//
//		final ResourceSet resSet = new ResourceSetImpl();
//		resSet.setResourceFactoryRegistry(reg);
//
//		final Resource resource = resSet.getResource(uriInstance, true);
//
//		return (UsageModel) resource.getContents().get(0);
//
//	}

}
