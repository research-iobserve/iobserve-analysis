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
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 *
 * @author Clemens
 * @author Eric Schmieders
 *
 */
public class Vertex {

    /** vertex stereotypes. */
    public enum EStereoType {
        GEOLOCATION, COMPUTING_NODE, DATASOURCE
    };

    private AllocationContext allocationcontext;

    private final String name;
    private final EStereoType stereotype;
    private PrivacyGraph graph;

    private final List<Edge> incoming = new ArrayList<>();
    private final List<Edge> outgoing = new ArrayList<>();

    /**
     * Create a new vertex with a name and stereotype.
     *
     * @param name
     *            unique name of the vertex
     * @param stereotype
     *            a stereotype
     */
    public Vertex(final String name, final EStereoType stereotype) {
        this.name = name;
        this.stereotype = stereotype;
    }

    /**
     * Add an outgoing edge.
     *
     * @param edge
     *            the edge
     */
    public void addOutgoing(final Edge edge) {
        this.outgoing.add(edge);
    }

    /**
     * Set graph to which this vertex belongs. This is a parent reference
     *
     * @param graph
     *            the graph
     */
    public void setGraph(final PrivacyGraph graph) {
        this.graph = graph;
    }

    public PrivacyGraph getGraph() {
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

    /**
     * Get all outgoing edges which have at least the given data classification.
     *
     * @param dataClassification
     *            the data classification
     *
     * @return returns all edges which match the criteria
     */
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

    /**
     * Find all direct neighbors.
     *
     * @return return a list of neighboring vertices
     */
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

    public AllocationContext getAllocationContext() {
        return this.allocationcontext;
    }

    public void setAllocationContext(final AllocationContext allocationcontext) {
        this.allocationcontext = allocationcontext;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public EStereoType getStereoType() {
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
