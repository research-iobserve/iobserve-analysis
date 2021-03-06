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
package org.iobserve.analysis.systems.jira;

import java.util.HashMap;
import java.util.Map;

import org.iobserve.analysis.behavior.models.data.IParameterValueDoubleMapper;

/**
 * This class maps parameter values to numbers reflecting the dissimilarity of the elements. The
 * mapping reflects the JPetStore.
 *
 * @author Reiner Jung
 *
 */
public class JIRAParameterValueDoubleMapper implements IParameterValueDoubleMapper {

    private final Map<String, Map<String, Double>> valueMap = new HashMap<>();

    /**
     * Constructor of the value double mapper.
     */
    public JIRAParameterValueDoubleMapper() {
        // empty constructor
    }

    @Override
    public double mapValue(final String parameter, final String value) {
        return this.valueMap.get(parameter).get(value);
    }

}
