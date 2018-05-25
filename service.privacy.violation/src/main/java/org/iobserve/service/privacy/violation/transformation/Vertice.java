package org.iobserve.service.privacy.violation.transformation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Clemens
 *
 */
public class Vertice {
    private final String name;
    private final List<Edge> incoming = new ArrayList<>(), outgoing = new ArrayList<>();

    public Vertice(final String name) {
        this.name = name;

    }

    public void addOutgoing(final Edge e) {
        this.outgoing.add(e);
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
}
