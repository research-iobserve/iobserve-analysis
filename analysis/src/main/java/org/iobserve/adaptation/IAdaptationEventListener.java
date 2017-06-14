package org.iobserve.adaptation;

import java.util.List;

import org.iobserve.adaptation.execution.ActionScript;

/**
 * Interface for an event listener that is used while executing the adaptation
 * stage.
 *
 * @author Tobias Poeppke
 * @author Robert Heinrich
 */
public interface IAdaptationEventListener {
	/**
	 * Notifies the listener of actions in the adaptation plan that can not be
	 * executed automatically.
	 *
	 * The listener may decide to throw a runtime exception to abort the
	 * adaptation process or return normally to continue.
	 *
	 * @param unsupportedActions
	 *            list of unsupported actions
	 */
	void notifyUnsupportedActionsFound(List<ActionScript> unsupportedActions);

	/**
	 * Notifies the listener of an error during script execution.
	 *
	 * The listener may decide to throw a runtime exception to abort the
	 * adaptation process or return normally to continue.
	 *
	 * @param script
	 *            the script for which the error was encountered
	 * @param e
	 *            the exception raised by the script
	 */
	void notifyExecutionError(ActionScript script, Throwable e);
}
