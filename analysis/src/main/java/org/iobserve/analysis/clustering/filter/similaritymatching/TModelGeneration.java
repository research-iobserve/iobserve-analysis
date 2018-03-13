package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

//<BehaviorModelTable[],List<List<Integer>>>
public class TModelGeneration extends AbstractStage {
    private final OutputPort<BehaviorModel[]> outputPort = this.createOutputPort();
    private final InputPort<BehaviorModel[]> modelsInputPort = this.createInputPort(BehaviorModel[].class);
    private final InputPort<Integer[][]> groupsInputPort = this.createInputPort();

    private final IModelGenerationStrategy modelGenerationStrategy;
    private BehaviorModel[] models;
    private Integer[][] groups;

    public TModelGeneration(final IModelGenerationStrategy modelGenerationStrategy) {
        this.modelGenerationStrategy = modelGenerationStrategy;
    }

    @Override
    protected void execute() {
        this.models = this.modelsInputPort.receive();
        this.groups = this.groupsInputPort.receive();

        if ((this.models == null) || (this.groups == null)) {
            return;
        }

        final BehaviorModel[] representativeModels = new BehaviorModel[this.groups.length];

        for (int i = 0; i < this.groups.length; i++) {
            /** Put all models from this group into an array */
            final BehaviorModel[] groupModels = new BehaviorModel[this.groups[i].length];
            for (int j = 0; j < groupModels.length; j++) {
                groupModels[j] = this.models[this.groups[i][j]];
            }

            /** Find representative model with strategy */
            representativeModels[i] = this.modelGenerationStrategy.generateModel(groupModels);
            representativeModels[i].setName("Representative Model of group #" + i);
        }

        this.outputPort.send(representativeModels);

        /** Clear state */
        this.models = null;
        this.groups = null;
    }

    public OutputPort<BehaviorModel[]> getOutputPort() {
        return this.outputPort;
    }

    public InputPort<BehaviorModel[]> getModelsInputPort() {
        return this.modelsInputPort;
    }

    public InputPort<Integer[][]> getGroupsInputPort() {
        return this.groupsInputPort;
    }
}