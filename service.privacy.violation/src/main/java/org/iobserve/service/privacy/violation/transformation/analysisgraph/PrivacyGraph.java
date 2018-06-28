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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

    /**
     * Remove an edge from the graph.
     *
     * @param edge
     *            edge to be removed
     *
     * @return returns true on success else false
     */
    public boolean removeEdge(final Edge edge) {
        if (this.isConsistent() && this.getEdges().contains(edge)) {
            for (final Vertex v : this.getVertices().values()) {
                if (v.getIncomingEdges().contains(edge)) {
                    v.getIncomingEdges().remove(edge);
                }
                if (v.getOutgoingEdges().contains(edge)) {
                    v.getOutgoingEdges().remove(edge);
                }
            }
            this.edges.remove(edge);
            return true;
        }
        return false;
    }

    /**
     * Remove a vertex and all edges related to the vertex.
     *
     * @param vertex
     *            vertex to be removed
     * @return returns true on success else false
     */
    public boolean removeVertice(final Vertex vertex) {
        if (this.isConsistent() && this.getVertices().containsValue(vertex)) {
            final List<Edge> removals = new ArrayList<>();
            for (final Edge edge : this.getEdges()) {
                if (edge.getSource().equals(vertex)) {
                    removals.add(edge);
                }
                if (edge.getTarget().equals(vertex)) {
                    removals.add(edge);
                }
            }
            for (final Edge e : removals) {
                this.removeEdge(e);
            }
            this.vertices.remove(vertex.getName());
            return true;
        }
        return false;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public Map<String, Vertex> getVertices() {
        return this.vertices;
    }

    /**
     * Checks whether the graph is consistent.
     *
     * @return returns true for consistent graphs, else false.
     *
     *         TODO this method should be able to return different types of answers, i.e., missing
     *         vertices, missing incoming and outgoing edges TODO also all printlns must be removed
     */
    public boolean isConsistent() {
        return this.checkEdgeConsistency() && this.checkVertexConsistency();
    }

    private boolean checkVertexConsistency() {
        for (final Vertex v : this.vertices.values()) {
            for (final Edge e : v.getIncomingEdges()) {
                if (!this.edges.contains(e)) {
                    System.out.println("Edge Missing");
                    return false;
                }
            }
            for (final Edge e : v.getOutgoingEdges()) {
                if (!this.edges.contains(e)) {
                    System.out.println("Edge Missing_2");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkEdgeConsistency() {
        for (final Edge edge : this.edges) {
            final Vertex source = edge.getSource();
            final Vertex target = edge.getTarget();

            if (!this.vertices.containsValue(source) || !this.vertices.containsValue(target)) {
                System.out.println("Vertices Missing");
                return false;
            }

        }

        return true;
    }

    /**
     * Return the adjacency matrix of the current graph. The elements of the matrix indicate whether
     * pairs of vertices are adjacent or not in the graph.
     *
     * @return the adjacency matrix
     */
    public int[][] getAdjacencyMatrix() {
        final int size = this.getVertices().size();
        final int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][i] = 0;
            }
        }
        final List<Vertex> localVertices = new LinkedList<>(this.getVertices().values());
        for (final Vertex v : localVertices) {
            final int positionX = localVertices.indexOf(v);
            for (final Edge e : v.getOutgoingEdges()) {
                final int positionY = localVertices.indexOf(e.getTarget());
                if (matrix[positionX][positionY] != 0) {
                    continue;
                }
                matrix[positionX][positionY] = 1;
            }
            for (final Edge e : v.getIncomingEdges()) {
                final int positionY = localVertices.indexOf(e.getSource());
                if (matrix[positionX][positionY] != 0) {
                    continue;
                }
                matrix[positionX][positionY] = 1;
            }

        }

        return matrix;
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
     * Print adjacent matrix.
     */
    public void printAdjacentMatrix() {
        System.out.println(Arrays.deepToString(this.getAdjacencyMatrix()).replace("], ", "]\n"));
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
