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
package org.iobserve.analysis.behavior.filter.models;

/**
 * Maps string values for parameters to double values usable in clustering algorithms.
 *
 * @author Reiner Jung
 *
 */
public interface IParameterValueDoubleMapper {

    /**
     * Return a double value depending on the parameter and string value.
     *
     * @param parameter
     *            name of a request parameter
     * @param value
     *            value of the request parameter
     *
     * @return double number representing the value
     */
    double mapValue(String parameter, String value);
}