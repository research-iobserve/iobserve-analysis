package org.iobserve.service.behavior.analysis;

import teetime.framework.Configuration;
import teetime.stage.ElementsToList;

public class BehaviorAnalysisTeetimeConfiguration extends Configuration {
    public BehaviorAnalysisTeetimeConfiguration() {
        final UserSessionToModelConverter converter = new UserSessionToModelConverter();
        final BehaviorModelToOpticsDataConverter modelToOptics = new BehaviorModelToOpticsDataConverter();
        // TODO this isn't good
        final ElementsToList<OpticsData> elemToList = new ElementsToList<>(50);
        final MTreeGenerator<OpticsData> mtreegenerator = new MTreeGenerator<>(new OpticsData.OPTICSDataGED());
        final OpticsStage<OpticsData> opticsCluster = new OpticsStage<>(10.0, 10);

        this.connectPorts(converter.getOutputPort(), modelToOptics.getInputPort());
        this.connectPorts(modelToOptics.getOutputPort(), elemToList.getInputPort());
        this.connectPorts(elemToList.getOutputPort(), mtreegenerator.getInputPort());

        this.connectPorts(mtreegenerator.getOutputPort(), opticsCluster.getmTreeInputPort());
        this.connectPorts(elemToList.getOutputPort(), opticsCluster.getModelsInputPort());

    }

}
