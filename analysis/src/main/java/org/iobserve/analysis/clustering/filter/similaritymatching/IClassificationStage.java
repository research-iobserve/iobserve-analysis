package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public interface IClassificationStage {
    InputPort<UserSession> getSessionInputPort();

    InputPort<Long> getTimerInputPort();

    OutputPort<BehaviorModel[]> getOutputPort();
}
