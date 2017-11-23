package org.iobserve.evaluation;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelGraph;

import teetime.stage.basic.AbstractTransformation;

/**
 *
 * TODO add description.
 *
 * @author unknown.
 *
 */
public class ModelComparer extends AbstractTransformation<URI, Boolean> {

    private AdaptationData adaptationData;

    @Override
    protected void execute(final URI element) throws Exception {

        boolean equalGraphs = false;
        if (this.adaptationData != null) {
            // TODO finish
            final InitializeModelProviders modelProviders = new InitializeModelProviders(
                    new File(element.toFileString()));
            final GraphFactory graphFactory = new GraphFactory();
            final ModelGraph runtimeGraph = graphFactory.buildGraph(modelProviders);

            if (runtimeGraph.equals(this.adaptationData.getReDeploymentGraph())
                    && this.adaptationData.getReDeploymentGraph().equals(runtimeGraph)) {
                equalGraphs = true;
                SystemEvaluation.disableEvaluation();
            }
        }
        this.outputPort.send(new Boolean(equalGraphs));
    }

    public void setBaseData(final AdaptationData adaptationData) {
        this.adaptationData = adaptationData;
    }

}
