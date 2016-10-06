/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.Workload;
import org.palladiosimulator.pcm.usagemodel.impl.ClosedWorkloadImpl;
import org.palladiosimulator.pcm.usagemodel.impl.OpenWorkloadImpl;

/**
 * Evaluation of the workload specification. Calculates the relative measurement error between the
 * approach's calculated workload and a reference workload
 *
 * @author David Peter
 * @author Robert Heinrich
 */
public final class WorkloadEvaluation {

    /**
     * Default constructor.
     */
    private WorkloadEvaluation() {

    }

    /**
     * Calculates the relative measurement error between a reference workload and the approach's
     * calculated workload. For an open workload the relative error of the mean inter arrival time
     * is calculated. For a closed workload the relative error of the mean number of concurrent
     * users is calculated. RME = (mw - rw) / rw, mw = measured workload, rw = reference workload
     *
     * @param usageModel
     *            contains the calculated workload
     * @param referenceElements
     *            contains the reference workload
     * @return return the relative measurement error
     */
    public static double calculateRME(final UsageModel usageModel, final ReferenceElements referenceElements) {
        double rme = 0;
        final UsageScenario usageScenarioOfUsageModel = usageModel.getUsageScenario_UsageModel().get(0);
        final Workload workload = usageScenarioOfUsageModel.getWorkload_UsageScenario();
        // We distinguish between a closed and an open workload
        if (workload.getClass().equals(ClosedWorkloadImpl.class)) {
            final ClosedWorkload closedWorkloadOfUsageModel = (ClosedWorkload) workload;
            // The RME is calculated by the mean number of concurrent users that states the
            // population count of a closed workload
            final int usageModelWorkload = closedWorkloadOfUsageModel.getPopulation();
            final int referenceWorkload = referenceElements.getMeanConcurrentUserSessions();
            rme = ((1.0 * usageModelWorkload) - (1.0 * referenceWorkload)) / (1.0 * referenceWorkload);
        } else if (workload.getClass().equals(OpenWorkloadImpl.class)) {
            final OpenWorkload openWorkloadOfUsageModel = (OpenWorkload) workload;
            final String interArrivalTime = openWorkloadOfUsageModel.getInterArrivalTime_OpenWorkload()
                    .getSpecification();
            // The RME is calculated by the mean inter arrival time that states an open workload
            final long usageModelWorkload = Long.valueOf(interArrivalTime).longValue();
            final long referenceWorkload = referenceElements.getMeanInterArrivalTime();
            rme = ((1.0 * usageModelWorkload) - (1.0 * referenceWorkload)) / (1.0 * referenceWorkload);
        }

        rme = Math.abs(rme) * 100;
        return rme;
    }

}
