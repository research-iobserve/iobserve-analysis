package org.iobserve.planning.data;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;

/**
 * This class transports all required data between the planning filter stages.
 *
 * @author Philipp Weimann
 * @author Tobias Poeppke
 * @author Robert Heinrich
 */
public class PlanningData {

	// TODO configuration option
	public static final int POSSIBLE_REPLICAS_OFFSET = 10;
	public static final int POSSIBLE_REPLICAS_FACTOR = 1;

	private AdaptationData adaptationData;

	private URI perOpteryxDir;
	private URI lqnsDir;
	private URI privacyAnalysisFile;

	private URI originalModelDir;
	private URI processedModelDir;

	/**
	 * @return the wrapped adaptation data
	 */
	public AdaptationData getAdaptationData() {
		return this.adaptationData;
	}

	/**
	 * @param adaptationData
	 *            the wrapped adaptation data
	 */
	public void setAdaptationData(AdaptationData adaptationData) {
		this.adaptationData = adaptationData;
	}

	/**
	 * @return the headless PerOpteryx dir URI
	 */
	public URI getPerOpteryxDir() {
		return this.perOpteryxDir;
	}

	/**
	 * @param perOpteryxDir
	 *            the headless PerOpteryx dir URI
	 */
	public void setPerOpteryxDir(URI perOpteryxDir) {
		this.perOpteryxDir = perOpteryxDir;
	}

	/**
	 * @return the PerOpteryx input and processing PCM dir URI
	 */
	public URI getProcessedModelDir() {
		return this.processedModelDir;
	}

	/**
	 * @param processedModelDir
	 *            the PerOpteryx input and processing PCM dir URI
	 */
	public void setProcessedModelDir(URI processedModelDir) {
		this.processedModelDir = processedModelDir;
	}

	/**
	 * @return the original PCM dir URI
	 */
	public URI getOriginalModelDir() {
		return this.originalModelDir;
	}

	/**
	 * @param originalModelDir
	 *            the original PCM dir URI
	 */
	public void setOriginalModelDir(URI originalModelDir) {
		this.originalModelDir = originalModelDir;
	}

	/**
	 * @return the LQN Solver dir URI
	 */
	public URI getLqnsDir() {
		return lqnsDir;
	}

	/**
	 * @param lqnsDir
	 *            the LQN Solver dir URI
	 */
	public void setLqnsDir(URI lqnsDir) {
		this.lqnsDir = lqnsDir;
	}

	/**
	 * @return the privacyAnalysisFile
	 */
	public URI getPrivacyAnalysisFile() {
		return privacyAnalysisFile;
	}

	/**
	 * @param privacyAnalysisFile
	 *            the privacyAnalysisFile to set
	 */
	public void setPrivacyAnalysisFile(URI privacyAnalysisFile) {
		this.privacyAnalysisFile = privacyAnalysisFile;
	}

}
