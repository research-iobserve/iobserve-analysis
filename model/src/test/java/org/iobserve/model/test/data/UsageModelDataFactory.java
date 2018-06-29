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
package org.iobserve.model.test.data;

import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;

/**
 * @author Reiner Jung
 *
 */
public final class UsageModelDataFactory {

    public static final String BUY_A_BOOK_BEHAVIOR = "Buy a book";
    public static final String USAGE_SCENARIO_GROUP_0 = "Usage scenario of user group 0";
    public static final String QUERY_CALL = "getQueryCall";
    public static final String PRICE_CALL = "getPriceCall";

    /** factory class. */
    private UsageModelDataFactory() {
        // factory, do not instantiate
    }

    /**
     * Create a usage model.
     *
     * @return returns a usage model
     */
    public static UsageModel createUsageModel() {
        final UsageModel usageModel = UsagemodelFactory.eINSTANCE.createUsageModel();

        // Think time
        final PCMRandomVariable thinkTime = CoreFactory.eINSTANCE.createPCMRandomVariable();
        thinkTime.setSpecification("5.0");

        // Closed workload
        final ClosedWorkload closedWorkload = UsagemodelFactory.eINSTANCE.createClosedWorkload();
        closedWorkload.setPopulation(2);
        closedWorkload.setThinkTime_ClosedWorkload(thinkTime);

        // Scenario behavior
        final ScenarioBehaviour buyBookScenarioBehaviour = UsageModelDataFactory.createScenarionBehaviorBuyaBook();

        // Usage scenario
        final UsageScenario usageScenarioGroup0 = UsagemodelFactory.eINSTANCE.createUsageScenario();

        usageScenarioGroup0.setEntityName(UsageModelDataFactory.USAGE_SCENARIO_GROUP_0);
        usageScenarioGroup0.setScenarioBehaviour_UsageScenario(buyBookScenarioBehaviour);
        usageScenarioGroup0.setWorkload_UsageScenario(closedWorkload);

        usageModel.getUsageScenario_UsageModel().add(usageScenarioGroup0);

        return usageModel;
    }

    private static ScenarioBehaviour createScenarionBehaviorBuyaBook() {
        final ScenarioBehaviour behavior = UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
        behavior.setEntityName(UsageModelDataFactory.BUY_A_BOOK_BEHAVIOR);

        // Start, stop and entry level system calls
        final Start startScenario = UsagemodelFactory.eINSTANCE.createStart();
        startScenario.setEntityName("startScenario");

        final EntryLevelSystemCall getQueryCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
        getQueryCall.setEntityName(UsageModelDataFactory.QUERY_CALL);
        getQueryCall.setPredecessor(startScenario);

        final EntryLevelSystemCall getPriceCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
        getPriceCall.setEntityName(UsageModelDataFactory.PRICE_CALL);
        getPriceCall.setPredecessor(getQueryCall);

        final EntryLevelSystemCall withdrawCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
        withdrawCall.setEntityName("withdrawCall");
        withdrawCall.setPredecessor(getPriceCall);

        final Stop stopScenario = UsagemodelFactory.eINSTANCE.createStop();
        stopScenario.setEntityName("stopScenario");
        stopScenario.setPredecessor(withdrawCall);

        behavior.getActions_ScenarioBehaviour().add(startScenario);
        behavior.getActions_ScenarioBehaviour().add(getQueryCall);
        behavior.getActions_ScenarioBehaviour().add(getPriceCall);
        behavior.getActions_ScenarioBehaviour().add(withdrawCall);
        behavior.getActions_ScenarioBehaviour().add(stopScenario);

        return behavior;
    }

    public static ScenarioBehaviour findBehavior(final UsageModel usageModel, final String behaviorName) {
        for (final UsageScenario usageScenario : usageModel.getUsageScenario_UsageModel()) {
            final ScenarioBehaviour behavior = usageScenario.getScenarioBehaviour_UsageScenario();
            if (behavior.getEntityName().equals(behaviorName)) {
                return behavior;
            }

        }
        return null;

    }

    public static UsageScenario findUsageScenario(final UsageModel usageModel, final String usageName) {
        for (final UsageScenario usageScenario : usageModel.getUsageScenario_UsageModel()) {
            if (usageScenario.getEntityName().equals(usageName)) {
                return usageScenario;
            }
        }
        return null;
    }

    /**
     * @param usageModel
     *            usage model
     * @param usageScenarioName
     *            usage scenario to be found in the model
     * @param behaviorName
     *            behavior which should be in this usage scenario
     * @param actionName
     *            name of the action
     * @return the action if the action is an {@link EntryLevelSystemCall}
     */
    public static EntryLevelSystemCall findSystemCallbyName(final UsageModel usageModel, final String usageScenarioName,
            final String behaviorName, final String actionName) {
        for (final UsageScenario usageScenario : usageModel.getUsageScenario_UsageModel()) {
            if (usageScenario.getEntityName().equals(usageScenarioName)
                    && usageScenario.getScenarioBehaviour_UsageScenario().getEntityName().equals(behaviorName)) {
                for (final AbstractUserAction action : usageScenario.getScenarioBehaviour_UsageScenario()
                        .getActions_ScenarioBehaviour()) {
                    if (action.getEntityName().equals(actionName)) {
                        if (action instanceof EntryLevelSystemCall) {
                            return (EntryLevelSystemCall) action;
                        }
                    }
                }
            }
        }
        return null;
    }

}
