package org.iobserve.service.privacy.violation.transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {
    private final List<Edge> edges;
    private final List<Vertice> vertices;

    public Graph() {
        this.edges = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    public void addVertice(final Vertice v) {
        this.vertices.add(v);
    }

    public void addEdge(final Vertice source, final Vertice target) {
        this.edges.add(new Edge(source, target));
    }

    public void addEdge(final Edge e) {
        this.edges.add(e);
    }

    public boolean removeEdge(final Edge e) {
        if (this.isConsistent() && this.getEdges().contains(e)) {
            for (final Vertice v : this.getVertices()) {
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

    public boolean removeVertice(final Vertice v) {
        if (this.isConsistent() && this.getVertices().contains(v)) {
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

    public List<Vertice> getVertices() {
        return this.vertices;
    }

    public boolean isConsistent() {
        for (final Edge e : this.edges) {
            final Vertice source = e.getSource();
            final Vertice target = e.getTarget();
            if (!this.vertices.contains(source) || !this.vertices.contains(target)) {
                return false;
            }

        }
        for (final Vertice v : this.vertices) {
            for (final Edge e : v.getIncomingEdges()) {
                if (!this.edges.contains(e)) {
                    return false;
                }
            }
            for (final Edge e : v.getOutgoingEdges()) {
                if (!this.edges.contains(e)) {
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
     */

    public int[][] getAdjacencyMatrix() {
        final int size = this.getVertices().size();
        final int matrix[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][i] = 0;
            }
        }

        for (final Vertice v : this.getVertices()) {
            final int positionX = this.getVertices().indexOf(v);
            for (final Edge e : v.getOutgoingEdges()) {
                final int positionY = this.getVertices().indexOf(e.getTarget());
                if (matrix[positionX][positionY] != 0) {
                    continue;
                }
                matrix[positionX][positionY] = 1;
            }
            for (final Edge e : v.getIncomingEdges()) {
                final int positionY = this.getVertices().indexOf(e.getSource());
                if (matrix[positionX][positionY] != 0) {
                    continue;
                }
                matrix[positionX][positionY] = 1;
            }

        }

        return matrix;
    }

    public void printGraph() {
        for (final Vertice v : this.getVertices()) {
            // for (Edge e : v.getIncomingEdges()) {
            // System.out.println("\t" + "(" + e.getSource() + "," +
            // e.getTarget() + ")");
            // }
            System.out.println(v);
            for (final Edge e : v.getOutgoingEdges()) {
                System.out.println("\t" + e.getName());
            }
        }
    }

    public void printAdjacentMatrix() {
        System.out.println(Arrays.deepToString(this.getAdjacencyMatrix()).replace("], ", "]\n"));
    }

}
