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

public class Graph {

    private final String name;

    private final List<Edge> edges;
    private final Map<String, Vertex> vertices;

    public Graph(final String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.vertices = new LinkedHashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public void addVertice(final Vertex v) {
        v.setGraph(this);
        this.vertices.put(v.getName(), v);
    }

    public void addEdge(final Vertex source, final Vertex target) {
        this.edges.add(new Edge(source, target));
    }

    public void addEdge(final Edge e) {
        this.edges.add(e);
    }

    public boolean removeEdge(final Edge e) {
        if (this.isConsistent() && this.getEdges().contains(e)) {
            for (final Vertex v : this.getVertices().values()) {
                if (v.getIncomingEdges().contains(e)) {
                    v.getIncomingEdges().remove(e);
                }
                if (v.getOutgoingEdges().contains(e)) {
                    v.getOutgoingEdges().remove(e);
                }
            }
            this.edges.remove(e);
            return true;
        }
        return false;
    }

    public boolean removeVertice(final Vertex v) {
        if (this.isConsistent() && this.getVertices().containsValue(v)) {
            final List<Edge> removals = new ArrayList<>();
            for (final Edge e : this.getEdges()) {
                if (e.getSource().equals(v)) {
                    removals.add(e);
                }
                if (e.getTarget().equals(v)) {
                    removals.add(e);
                }
            }
            for (final Edge e : removals) {
                this.removeEdge(e);
            }
            this.vertices.remove(v);
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
        for (final Edge e : this.edges) {
            final Vertex source = e.getSource();
            final Vertex target = e.getTarget();
            if (!this.vertices.containsValue(source) || !this.vertices.containsValue(target)) {
                System.out.println("Vertices Missing");
                return false;
            }

        }
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

    public Vertex getVertexByName(final String vertexName) {
        return this.getVertices().get(vertexName);
    }

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

    public void printAdjacentMatrix() {
        System.out.println(Arrays.deepToString(this.getAdjacencyMatrix()).replace("], ", "]\n"));
    }

    public Map<String, Vertex> getComponentVerticesDeployedAt(final Policy.EGeoLocation geoLocation) {
        final Vertex vertice = this.getVertexByName(geoLocation.name());

        return vertice.getAllReachableVertices();
    }

}
