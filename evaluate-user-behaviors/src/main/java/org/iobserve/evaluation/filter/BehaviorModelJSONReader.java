/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.evaluation.filter;

import java.io.File;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import teetime.framework.AbstractProducerStage;

/**
 * Read a JSON serialized behavior model.
 *
 * @author Reiner Jung
 *
 */
public class BehaviorModelJSONReader extends AbstractProducerStage<BehaviorModel> {

    private final File inputFile;

    /**
     * Create a JSON reader stage.
     *
     * @param inputFile
     *            JSON file source
     */
    public BehaviorModelJSONReader(final File inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    protected void execute() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        final BehaviorModel model = new BehaviorModel();
        /** Have to read the model by hand */
        final JsonNode tree = mapper.readTree(this.inputFile);
        if (!(tree instanceof ObjectNode)) {
            throw new Exception("Wrong format");
        }
        final ObjectNode modelNode = (ObjectNode) tree;

        // Read name
        final JsonNode name = modelNode.findValue("name");
        if (name.isTextual()) {
            model.setName(name.textValue());
        }

        final JsonNode nodesList = modelNode.findValue("nodes");
        if (nodesList.isArray()) {

        }

        this.outputPort.send(model);

        this.workCompleted();
    }

}
