package org.iobserve.service.privacy.violation.transformation;

public class Edge {
    /** The reference to the source and target vertices **/
    private final Vertice source, target;
    private String name;

    /** Create a new Edge from source vertice to target vertice **/
    public Edge(final Vertice source, final Vertice target) {
        this.source = source;
        this.target = target;
        source.addOutgoing(this);
        target.addIncoming(this);
        this.name = "E(" + source.getName() + "->" + target.getName() + ")";
    }

    public Vertice getSource() {
        return this.source;
    }

    public Vertice getTarget() {
        return this.target;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String s) {
        this.name = s;
    }
}
