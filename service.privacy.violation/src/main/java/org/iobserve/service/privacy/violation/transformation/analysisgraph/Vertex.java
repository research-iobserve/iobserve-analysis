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
 * @author Clemens
 * @author Eric Schmieders
 *
 */
public class Vertex {

    /** vertex stereotypes. */
    public enum STEREOTYPES {
        Geolocation, ComputingNode, Datasource
    };

    private final String name;
    private final STEREOTYPES stereotype;
    private Graph graph;

    private final List<Edge> incoming = new ArrayList<>();
    private final List<Edge> outgoing = new ArrayList<>();

    public Vertex(final String name, final STEREOTYPES stereotype) {
        this.name = name;
        this.stereotype = stereotype;
    }

    public void addOutgoing(final Edge edge) {
        this.outgoing.add(edge);
    }

    public void setGraph(final Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return this.graph;
    }

    /**
     * Add an incoming edge to a vertex.
     *
     * @param edge
     *            the edge to be added
     */
    public void addIncoming(final Edge edge) {
        this.incoming.add(edge);
    }

    /**
     * @return returns the name of this vertex
     **/
    public String getName() {
        return this.name;
    }

    /**
     * @return returns the outgoing edges leading from this vertex
     **/
    public List<Edge> getOutgoingEdges() {
        return this.outgoing;
    }

    /**
     * @return returns the incoming edges leading to this vertex
     **/
    public List<Edge> getIncomingEdges() {
        return this.incoming;
    }

    public List<Edge> getOutgoingEdgesClassifiedAtLeast(final Policy.EDataClassification dataClassification) {
        final List<Edge> edges = new ArrayList<>();

        for (final Edge edge : this.getOutgoingEdges()) {
            if (Policy.isEqualOrMoreCritical(dataClassification, edge.getDPC())) {
                edges.add(edge);
            }
        }

        return edges;
    }

    /**
     * Returns a list containing all edges either ending at or starting from this vertex. The list
     * contains first all the incoming ones, second the outing ones.
     *
     * @return returns list of edges
     **/
    public List<Edge> getAllEdges() {
        final List<Edge> allEdges = new ArrayList<>();
        allEdges.addAll(this.incoming);
        allEdges.addAll(this.outgoing);
        return allEdges;
    }

    public List<Vertex> neighbourhood() {
        final List<Vertex> out = new ArrayList<>();
        for (final Edge e : this.getOutgoingEdges()) {
            out.add(e.getTarget());
        }
        for (final Edge e : this.getIncomingEdges()) {
            out.add(e.getSource());
        }
        return out;
    }

    /**
     * Degree of a vertex defined by its neighborhood. Faster would be the number of
     * {@link #getAllEdges()} (see {@link #degreeF()}).
     *
     * @return returns the degree of a vertex
     **/
    public int degree() {
        return this.neighbourhood().size();
    }

    /**
     * Efficient version of {@link #degree()}, but not following the definition.
     *
     * @return returns the number of edges belonging to a vertex
     **/
    public int degreeF() {
        return this.getAllEdges().size();
    }

    @Override
    public String toString() {
        return this.name;
    }

    public STEREOTYPES getStereoType() {
        return this.stereotype;
    }

    /**
     * Computes map of all vertices which are directly linked by an edge by this vertex.
     *
     * @return returns a map of vertices with the edge name as key
     */
    public Map<String, Vertex> getAllReachableVertices() {
        final Map<String, Vertex> reachableVertices = new LinkedHashMap<>();

        for (final Edge edge : this.getOutgoingEdges()) {
            reachableVertices.put(edge.getTarget().name, edge.getTarget());
        }

        return reachableVertices;
    }

}
