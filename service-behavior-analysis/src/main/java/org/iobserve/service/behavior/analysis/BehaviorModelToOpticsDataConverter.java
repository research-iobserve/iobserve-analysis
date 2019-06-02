package org.iobserve.service.behavior.analysis;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.stage.basic.AbstractTransformation;

public class BehaviorModelToOpticsDataConverter extends AbstractTransformation<BehaviorModelGED, OpticsData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorModelToOpticsDataConverter.class);

    @Override
    protected void execute(final BehaviorModelGED model) throws Exception {
        this.outputPort.send(new OpticsData(model));
        BehaviorModelToOpticsDataConverter.LOGGER.info("Converted BehaviorModelGED to OpticsData");

    }

}
