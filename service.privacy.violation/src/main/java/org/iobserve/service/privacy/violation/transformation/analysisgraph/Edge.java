package org.iobserve.service.privacy.violation.transformation.analysisgraph;

import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;

/**
 *
 * @author Clemens
 * @author Eric Schmieders
 *
 */
public class Edge {
    /** The reference to the source and target vertices **/
    private final Vertice source, target;
    private String name;
    private Policy.DATACLASSIFICATION dataProtectionClass = Policy.DATACLASSIFICATION.values()[0];

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

    public void setDPC(final Policy.DATACLASSIFICATION s) {
        this.dataProtectionClass = s;
    }

    public Policy.DATACLASSIFICATION getDPC() {
        return this.dataProtectionClass;
    }

	public Object getPrint() {
		return "Edge '"+getName()+"' from '"+getSource()+"' to '"+getTarget()+"' with data protection class '"+getDPC()+"'";
	}
}
