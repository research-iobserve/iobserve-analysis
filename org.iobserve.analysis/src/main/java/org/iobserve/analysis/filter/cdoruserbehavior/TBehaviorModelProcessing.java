/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/
package org.iobserve.analysis.filter.cdoruserbehavior;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.cdoruserbehavior.BehaviorModelTable;
import org.iobserve.analysis.filter.models.cdoruserbehavior.EditableBehaviorModelTable;
import org.iobserve.analysis.filter.models.cdoruserbehavior.JPetstoreStrategy;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 *
 * @author Christoph Dornieden
 */

public class TBehaviorModelProcessing extends AbstractConsumerStage<EntryCallSequenceModel> {

    /** logger. */
    private static final Log LOG = LogFactory.getLog(RecordSwitch.class);

    private final TBehaviorModelTableGeneration tBehaviorModelGeneration;
    private final TBehaviorModelPreperation tBehaviorModelPreperation;
    /** output port. */
    private final OutputPort<BehaviorModelTable> behaviorModelOutputPort = this.createOutputPort();

    /**
     * constructor
     */
    public TBehaviorModelProcessing() {
        final List<String> filterBlackList = new ArrayList<>();
        filterBlackList.add("(jpetstore\\.images).*\\)");
        filterBlackList.add("(jpetstore\\.css).*\\)");

        final EditableBehaviorModelTable modelTable = new EditableBehaviorModelTable(new JPetstoreStrategy(),
                filterBlackList, true);
        this.tBehaviorModelGeneration = new TBehaviorModelTableGeneration(modelTable);
        this.tBehaviorModelPreperation = new TBehaviorModelPreperation(
                modelTable.getClearedFixedSizeBehaviorModelTable());

    }

    @Override
    protected void execute(final EntryCallSequenceModel element) {
        // TODO Auto-generated method stub
    }

}
