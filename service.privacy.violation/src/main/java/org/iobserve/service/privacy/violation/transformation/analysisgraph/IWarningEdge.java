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
package org.iobserve.service.privacy.violation.transformation.analysisgraph;

import java.util.List;

/**
 *
 * @author Clemens
 *
 */
public interface IWarningEdge {
    /**
     * Returns the list of violated edges.
     *
     * @return the contained (error) edge.
     */
    List<Edge> getWarningEdges();

    /**
     * Sets the list of warning edges.
     *
     * @param messages
     *            the list of edges.
     */
    void setWarningEdges(List<Edge> edges);

    /**
     * Adds one edge to the list.
     *
     * @param message
     *            the new edge in the list.
     */
    void addWarningEdge(Edge edge);
}
