package org.iobserve.analysis.clustering.filter;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class TGroupingStage extends AbstractConsumerStage<Double[][]> {
    private final OutputPort<List<List<Integer>>> outputPort = this.createOutputPort();

    private final double similarityRadius;

    private final List<List<Integer>> groups = new ArrayList<>();

    private Double[][] vectors;

    public TGroupingStage(final double similarityRadius) {
        super();

        // TODO: check arg validity (non-negative)
        this.similarityRadius = similarityRadius;
    }

    @Override
    protected void execute(final Double[][] vectors) {
        this.vectors = vectors;

        for (int i = 0; i < vectors.length; i++) {
            final List<Integer> group = this.findGroup(vectors[i]);

            if (group == null) {
                final List<Integer> newGroup = new ArrayList<>();
                newGroup.add(i);
                this.groups.add(newGroup);
            } else {
                group.add(i);
            }
        }

        this.outputPort.send(this.groups);
    }

    private List<Integer> findGroup(final Double[] vector) {
        /** First matching group found is selected */
        for (final List<Integer> group : this.groups) {
            boolean matchAll = true;
            for (final Integer index : group) {
                if (!this.match(vector, this.vectors[index])) {
                    matchAll = false;
                    break;
                }
            }

            if (matchAll) {
                return group;
            }
        }

        return null;
    }

    private boolean match(final Double[] a, final Double[] b) {
        /** a and b should always have same length */
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - b[i]) >= this.similarityRadius) {
                return false;
            }
        }

        return true;
    }

    public OutputPort<List<List<Integer>>> getOutputPort() {
        return this.outputPort;
    }
}