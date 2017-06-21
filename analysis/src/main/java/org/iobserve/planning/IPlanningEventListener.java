package org.iobserve.planning;

import java.util.List;

import org.iobserve.adaptation.execution.ActionScript;
import org.iobserve.analysis.InitializeModelProviders;

/**
 * Interface for an event listener that is used while planning/generating a
 * re-deployment model.
 *
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public interface IPlanningEventListener {
	/**
	 * Notifies the listener that no valid pcm model was found during generation.
	 *
	 * The listener may decide to throw a runtime exception to abort the
	 * adaptation process or return normally to continue.
	 *
	 * @param modelProviders
	 *            the in-compliant model part
	 */
	void notifyNonValidRedeploymentModelFound(InitializeModelProviders modelProviders);
	
	/**
	 * Notifies the listener that no PCM model was found!
	 *
	 *
	 * @param error
	 *            the error message
	 */
	void notifyNonValidRedeploymentModelFound(String error);
}
