/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;

/**
 *
 * @author Clemens Brackmann
 * @author Eric Schmieders
 *
 */
// TODO maybe convert to POJO + factory
public class PrivacyGraph {

    private final String name;

    private final List<Edge> edges;
    private final Map<String, Vertex> vertices;

    /**
     * Create a named dataflow graph.
     *
     * @param name
     *            name of the graph
     */
    public PrivacyGraph(final String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.vertices = new LinkedHashMap<>();
    }

    public String getName() {
        return this.name;
    }

    /**
     * Add a vertex to a graph.
     *
     * @param vertex
     *            the vertex
     */
    public void addVertex(final Vertex vertex) {
        vertex.setGraph(this);
        this.vertices.put(vertex.getName(), vertex);
    }

    /**
     * Create and add an edge to the graph.
     *
     * @param source
     *            source vertex
     * @param target
     *            target vertex
     */
    public void addEdge(final Vertex source, final Vertex target) {
        this.edges.add(new Edge(source, target));
    }

    /**
     * Add an externally created edge.
     *
     * @param edge
     *            the edge to be added
     */
    public void addEdge(final Edge edge) {
        this.edges.add(edge);
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public Map<String, Vertex> getVertices() {
        return this.vertices;
    }

    /**
     * get a vertex by name.
     *
     * @param vertexName
     *            the name of the vertex
     *
     * @return returns a vertex on success, else null
     */
    public Vertex getVertexByName(final String vertexName) {
        return this.getVertices().get(vertexName);
    }

    /**
     * Print all vertices.
     */
    public void printGraph() {
        for (final Vertex v : this.getVertices().values()) {
            // for (Edge e : v.getIncomingEdges()) {
            // System.out.println("\t" + "(" + e.getSource() + "," +
            // e.getTarget() + ")");
            // }
            System.out.println(v);
            for (final Edge e : v.getOutgoingEdges()) {
                System.out.println("\t" + e.getPrint());
            }
        }
    }

    /**
     * Get a map of vertices which are located at the given geolocation.
     *
     * @param geoLocation
     *            the geolocation
     * @return the map.
     */
    public Map<String, Vertex> getComponentVerticesDeployedAt(final Policy.EGeoLocation geoLocation) {
        final Vertex vertice = this.getVertexByName(geoLocation.name());

        return vertice.getAllReachableVertices();
    }

}
