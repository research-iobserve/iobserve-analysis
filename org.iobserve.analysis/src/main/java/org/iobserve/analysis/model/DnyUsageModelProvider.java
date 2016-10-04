package org.iobserve.analysis.model;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

// TODO was removed in master

public final class DnyUsageModelProvider extends AbstractEcoreModelProvider {

    public static final String PACKAGE_USAGEMODEL = "usagemodel";
    public static final String CL_WORKLOAD = "Workload";
    public static final String CL_USAGE_SCENARIO = "UsageScenario";
    public static final String CL_USAGE_DATE = "UserData";
    public static final String CL_USAGE_MODEL = "UsageModel";
    public static final String CL_ENTRY_LEVEL_SYSTEM_CALL = "EntryLevelSystemCall";
    public static final String CL_ABSTRACT_USER_ACTION = "AbstractUserAction";
    public static final String CL_SCENARIO_BEHAVIOUR = "ScenarioBehaviour";
    public static final String CL_BRANCH_TRANSITION = "BranchTransition";
    public static final String CL_BRANCH = "Branch";
    public static final String CL_LOOP = "Loop";
    public static final String CL_STOP = "Stop";
    public static final String CL_START = "Start";
    public static final String CL_OPEN_WORKLOAD = "OpenWorkload";
    public static final String CL_DELAY = "Delay";
    public static final String CL_CLOSED_WORKLOAD = "ClosedWorkload";

    public static final String SETTER_USAGE_SCENARIO = "usageScenario_UsageModel";
    public static final String SETTER_SCENARIO_BEHAVIOUR = "scenarioBehaviour_UsageScenario";
    public static final String SETTER_ABSTRACT_ACTION = "actions_ScenarioBehaviour";
    public static final String SETTER_WORKLOAD_USAGE_SCENARIO = "workload_UsageScenario";
    public static final String SETTER_INTERARRIVALTIME_OPEN_WORKLOAD = "interArrivalTime_OpenWorkload";

    public static final String SETTER_PREDECESSOR = "predecessor";
    public static final String SETTER_SUCCESSOR = "successor";

    private EObject usageModel;
    private EObject usageScenario;
    private EObject scenarioBehaviour;
    private EObject workload;
    private EObject loop;
    private EObject branchTransition;
    private EObject lastAction;

    public DnyUsageModelProvider() {
    }

    // ********************************************************************
    // * SETTER / GETTER
    // ********************************************************************

    public final void setUsageModel(final EObject usageModel) {
        final EClass clazzUsageModel = this.getEClass(PACKAGE_USAGEMODEL, CL_USAGE_MODEL);
        this.checkInstance(clazzUsageModel, usageModel);
        this.usageModel = usageModel;
    }

    public EObject getUsageModel() {
        return this.usageModel;
    }

    // ********************************************************************
    // * UPDATE
    // ********************************************************************

    private void updateLastAction(final EObject eObj) {
        if (this.lastAction != null) {
            final String objId = EcoreUtil.getID(eObj);
            this.setEObjectValue(this.lastAction, SETTER_SUCCESSOR, objId);
            final String lastObjId = EcoreUtil.getID(this.lastAction);
            this.setEObjectValue(eObj, SETTER_PREDECESSOR, lastObjId);
        }
        this.lastAction = eObj;
    }

    // ********************************************************************
    // * MODEL BUILDING
    // ********************************************************************

    public final void addUsageScenario(final EObject eObj) {
        final EClass clazzUsageScenario = this.getEClass(PACKAGE_USAGEMODEL, CL_USAGE_SCENARIO);
        this.checkInstance(clazzUsageScenario, eObj);
        this.usageScenario = eObj;
        this.addEListValue(this.usageModel, SETTER_USAGE_SCENARIO, this.usageScenario);
    }

    public final void setScenarioBehaviour(final EObject eObj) {
        final EClass clazzScenarioBehaviour = this.getEClass(PACKAGE_USAGEMODEL, CL_SCENARIO_BEHAVIOUR);
        this.checkInstance(clazzScenarioBehaviour, eObj);
        this.scenarioBehaviour = eObj;
        this.setEObjectValue(this.usageScenario, SETTER_SCENARIO_BEHAVIOUR, this.scenarioBehaviour);
    }

    public final void setOpenWorkload(final EObject eObj) {
        final EClass clazzOpenWorkload = this.getEClass(PACKAGE_USAGEMODEL, CL_OPEN_WORKLOAD);
        this.checkInstance(clazzOpenWorkload, eObj);
        this.setEObjectValue(this.usageScenario, SETTER_WORKLOAD_USAGE_SCENARIO, eObj);
        this.workload = eObj;
    }

    public final void setOpenWorkload(final EObject eObj, final float averageInterarrivalTime) {
        final EClass clazzOpenWorkload = this.getEClass(PACKAGE_USAGEMODEL, CL_OPEN_WORKLOAD);
        this.checkInstance(clazzOpenWorkload, eObj);
        this.setEObjectValue(this.usageScenario, SETTER_WORKLOAD_USAGE_SCENARIO, eObj);
        this.setEObjectValue(eObj, SETTER_INTERARRIVALTIME_OPEN_WORKLOAD, Float.valueOf(averageInterarrivalTime));
        this.workload = eObj;
    }

    public final void addStartAction(final EObject eObj) {
        final EClass clazzStartAction = this.getEClass(PACKAGE_USAGEMODEL, CL_START);
        this.checkInstance(clazzStartAction, eObj);
        this.addEListValue(this.scenarioBehaviour, SETTER_ABSTRACT_ACTION, eObj);
        this.updateLastAction(eObj);
    }

    public final void addEntryLevelSystemCallAction(final EObject eObj) {
        final EClass clazzEntryLevelSystemCall = this.getEClass(PACKAGE_USAGEMODEL, CL_ENTRY_LEVEL_SYSTEM_CALL);
        this.checkInstance(clazzEntryLevelSystemCall, eObj);
        this.addEListValue(this.scenarioBehaviour, SETTER_ABSTRACT_ACTION, eObj);
        this.updateLastAction(eObj);
    }

    public final void addEntryLevelSystemCallAction(final EObject eObj, final String actionId) {
        // TODO what to do?
    }

    public final void addStopAction(final EObject eObj) {
        final EClass clazzStopAction = this.getEClass(PACKAGE_USAGEMODEL, CL_STOP);
        this.checkInstance(clazzStopAction, eObj);
        this.addEListValue(this.scenarioBehaviour, SETTER_ABSTRACT_ACTION, eObj);
        this.updateLastAction(eObj);
    }

    public final void addBranch(final EObject eObj) {
        final EClass clazzBranch = this.getEClass(PACKAGE_USAGEMODEL, CL_BRANCH);
        this.checkInstance(clazzBranch, eObj);
        this.addEListValue(this.scenarioBehaviour, SETTER_ABSTRACT_ACTION, eObj);
        this.updateLastAction(eObj);
    }

    public final void addBranchTransition(final EObject eObj) {
        final EClass clazzBranchTransition = this.getEClass(PACKAGE_USAGEMODEL, CL_BRANCH_TRANSITION);
        this.checkInstance(clazzBranchTransition, eObj);
        this.addEListValue(this.scenarioBehaviour, SETTER_ABSTRACT_ACTION, eObj);// TODO is this
                                                                                 // right method`?
        this.updateLastAction(eObj);
    }

    // ********************************************************************
    // * ECORE MODEL SPECIFIC OBJECTS
    // ********************************************************************

    public final EObject createEcoreUsageModel() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_USAGE_MODEL, false);
    }

    public final EObject createEcoreUsageScenario() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_USAGE_SCENARIO, false);
    }

    public final EObject createEcoreOpenWorkload() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_OPEN_WORKLOAD, false);
    }

    public final EObject createEcoreScenarioBehavoir() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_SCENARIO_BEHAVIOUR, false);
    }

    public final EObject createEcoreStartAction() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_START, true);
    }

    public final EObject createEcoreStopAction() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_STOP, true);
    }

    public final EObject createEcoreLoopAction() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_LOOP, true);
    }

    public final EObject createEcoreBranch() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_BRANCH, true);
    }

    public final EObject createEcoreBranchTransition() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_BRANCH_TRANSITION, true);
    }

    public final EObject createEcoreEntryLevelSystemCallAction() {
        return this.createPartModel(PACKAGE_USAGEMODEL, CL_ENTRY_LEVEL_SYSTEM_CALL, true);
    }

}
