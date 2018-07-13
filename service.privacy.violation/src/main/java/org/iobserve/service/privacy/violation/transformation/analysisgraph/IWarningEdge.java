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
