package org.iobserve.planning.data;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;

/**
 * This class transports all required data between the planning filter stages.
 *
 * @author Philipp Weimann
 * @author Tobias Pöppke
 */
public class PlanningData {

	// TODO configuration option
	public static final int POSSIBLE_REPLICAS_OFFSET = 10;
	public static final int POSSIBLE_REPLICAS_FACTOR = 1;

	private AdaptationData adaptationData;

	private URI perOpteryxDir;

	private URI originalModelDir;
	private URI processedModelDir;

	public AdaptationData getAdaptationData() {
		return this.adaptationData;
	}

	public void setAdaptationData(AdaptationData adaptationData) {
		this.adaptationData = adaptationData;
	}

	public URI getPerOpteryxDir() {
		return this.perOpteryxDir;
	}

	public void setPerOpteryxDir(URI perOpteryxDir) {
		this.perOpteryxDir = perOpteryxDir;
	}

	public URI getProcessedModelDir() {
		return this.processedModelDir;
	}

	public void setProcessedModelDir(URI processedModelDir) {
		this.processedModelDir = processedModelDir;
	}

	public URI getOriginalModelDir() {
		return this.originalModelDir;
	}

	public void setOriginalModelDir(URI originalModelDir) {
		this.originalModelDir = originalModelDir;
	}

}
