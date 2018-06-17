package org.iobserve.service.privacy.violation.transformation.analysisgraph;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;

/**
 *
 * @author Clemens
 * @author Eric Schmieders
 *
 */
public class Vertice {

    public static enum STEREOTYPES {
        Geolocation, ComputingNode, Datasource
    };

    private final String name;
    private final STEREOTYPES stereotype;
    private Graph g;

    private final List<Edge> incoming = new ArrayList<>(), outgoing = new ArrayList<>();

    public Vertice(final String name, final STEREOTYPES stereotype) {
        this.name = name;
        this.stereotype = stereotype;
    }

    public void addOutgoing(final Edge e) {
        this.outgoing.add(e);
    }

    public void setGraph(final Graph g) {
        this.g = g;
    }

    public Graph getGraph() {
        return this.g;
    }

    public void addIncoming(final Edge e) {
        this.incoming.add(e);
    }

    /** Returns the name of this vertice **/
    public String getName() {
        return this.name;
    }

    /** Returns the outgoing edges leading from this vertice **/
    public List<Edge> getOutgoingEdges() {
        return this.outgoing;
    }

    /** Returns the incoming edges leading to this vertice **/
    public List<Edge> getIncomingEdges() {
        return this.incoming;
    }

    public List<Edge> getOutgoingEdgesClassifiedAtLeast(final Policy.DATACLASSIFICATION dataClassification) {
        final List<Edge> edges = new ArrayList<>();

        for (final Edge edge : this.getOutgoingEdges()) {
            if (Policy.isEqualOrMoreCritical(dataClassification, edge.getDPC())) {
                edges.add(edge);
            }
        }

        return edges;
    }

    /**
     * Returns a List containing all Edges either leading to or leading from the Vertice. First the
     * incoming ones, second the outing ones
     **/
    public List<Edge> getAllEdges() {
        final List<Edge> allEdges = new ArrayList<>();
        allEdges.addAll(this.incoming);
        allEdges.addAll(this.outgoing);
        return allEdges;
    }

    public List<Vertice> neighbourhood() {
        final List<Vertice> out = new ArrayList<>();
        for (final Edge e : this.getOutgoingEdges()) {
            out.add(e.getTarget());
        }
        for (final Edge e : this.getIncomingEdges()) {
            out.add(e.getSource());
        }
        return out;
    }

    /**
     * Degree of a vertice defined by its neighbourhood. Faster would be the number of
     * {@link #getAllEdges()} (see {@link #degreeF()}).
     **/
    public int degree() {
        return this.neighbourhood().size();
    }

    /**
     * Efficient version of {@link #degree()}, but not following the definition
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

    public LinkedHashMap<String, Vertice> getAllReachableVertices() {
        final LinkedHashMap<String, Vertice> reachableVertices = new LinkedHashMap<>();

        for (final Edge edge : this.getOutgoingEdges()) {
            reachableVertices.put(edge.getTarget().name, edge.getTarget());
        }

        return reachableVertices;
    }

}
