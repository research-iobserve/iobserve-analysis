package org.iobserve.analysis.userbehavior.test;

import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.Workload;
import org.palladiosimulator.pcm.usagemodel.impl.ClosedWorkloadImpl;
import org.palladiosimulator.pcm.usagemodel.impl.OpenWorkloadImpl;

/**
 * Evaluation of the workload specification. 
 * 
 * @author David
 *
 */
public class WorkloadEvaluation {
	
	/**
	 * Calculates the relative measurement error between a reference workload and the approach's calculated workload
	 * For an open workload the relative error of the mean inter arrival time is calculated 
	 * For a closed workload the relative error of the mean number of concurrent users is calculated 
	 * 
	 * @param usageModel contains the calculated workload
	 * @param referenceElements contains the reference workload
	 */
	public double calculateRME(final UsageModel usageModel, final ReferenceElements referenceElements) {
		double rme = 0;
		UsageScenario usageScenarioOfUsageModel = usageModel.getUsageScenario_UsageModel().get(0);
		Workload workload = usageScenarioOfUsageModel.getWorkload_UsageScenario();
		if(workload.getClass().equals(ClosedWorkloadImpl.class)) {
			ClosedWorkload closedWorkloadOfUsageModel = (ClosedWorkload) workload;
			int usageModelWorkload = closedWorkloadOfUsageModel.getPopulation();
			int referenceWorkload = referenceElements.getMeanConcurrentUserSessions();
			rme = (1.0*usageModelWorkload-1.0*referenceWorkload)/(1.0*referenceWorkload);
		} else if(workload.getClass().equals(OpenWorkloadImpl.class)) {
			OpenWorkload openWorkloadOfUsageModel = (OpenWorkload) workload;
			String interArrivalTime = openWorkloadOfUsageModel.getInterArrivalTime_OpenWorkload().getSpecification();
			long usageModelWorkload = Long.valueOf(interArrivalTime).longValue();
			long referenceWorkload = referenceElements.getMeanInterArrivalTime();
			rme = (1.0*usageModelWorkload-1.0*referenceWorkload)/(1.0*referenceWorkload);
		}
		if(rme<0)
			rme = rme * (-100);
		else 
			rme = rme * 100;
		
		return rme;
	}

}
