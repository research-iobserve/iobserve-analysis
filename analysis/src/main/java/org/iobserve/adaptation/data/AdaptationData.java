package org.iobserve.adaptation.data;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;

/**
 * This class provides all data required for planning and adapting the system.
 *
 * @author Philipp Weimann
 */
public class AdaptationData {
	public static final String NODE_STARTUP_SCRIPT_NAME = "node_startup";

	private URI runtimeModelURI;
	private ModelGraph runtimeGraph;

	private URI reDeploymentURI;
	private ModelGraph reDeploymentGraph;

	private URI deployablesFolderURI;

	private List<AssemblyContextAction> acActions;
	private List<ResourceContainerAction> rcActions;

	private List<Action> executionOrder;

	////////////////////// GETTERS & SETTERS //////////////////////
	/**
	 * @return the runtimeModelURI
	 */
	public URI getRuntimeModelURI() {
		return this.runtimeModelURI;
	}

	/**
	 * @param runtimeModelURI
	 *            the runtimeModelURI to set
	 */
	public void setRuntimeModelURI(URI runtimeModelURI) {
		this.runtimeModelURI = runtimeModelURI;
	}

	/**
	 * @return the runtimeGraph
	 */
	public ModelGraph getRuntimeGraph() {
		return this.runtimeGraph;
	}

	/**
	 * @param runtimeGraph
	 *            the runtimeGraph to set
	 */
	public void setRuntimeGraph(ModelGraph runtimeGraph) {
		this.runtimeGraph = runtimeGraph;
	}

	/**
	 * @return the reDeploymentURI
	 */
	public URI getReDeploymentURI() {
		return this.reDeploymentURI;
	}

	/**
	 * @param reDeploymentURI
	 *            the reDeploymentURI to set
	 */
	public void setReDeploymentURI(URI reDeploymentURI) {
		this.reDeploymentURI = reDeploymentURI;
	}

	/**
	 * @return the reDeploymentGraph
	 */
	public ModelGraph getReDeploymentGraph() {
		return this.reDeploymentGraph;
	}

	/**
	 * @param reDeploymentGraph
	 *            the reDeploymentGraph to set
	 */
	public void setReDeploymentGraph(ModelGraph reDeploymentGraph) {
		this.reDeploymentGraph = reDeploymentGraph;
	}

	/**
	 * @return the acActions
	 */
	public List<AssemblyContextAction> getAcActions() {
		return this.acActions;
	}

	/**
	 * @param acActions
	 *            the acActions to set
	 */
	public void setAcActions(List<AssemblyContextAction> acActions) {
		this.acActions = acActions;
	}

	/**
	 * @return the rcActions
	 */
	public List<ResourceContainerAction> getRcActions() {
		return this.rcActions;
	}

	/**
	 * @param rcActions
	 *            the rcActions to set
	 */
	public void setRcActions(List<ResourceContainerAction> rcActions) {
		this.rcActions = rcActions;
	}

	/**
	 * @return the executionOrder
	 */
	public List<Action> getExecutionOrder() {
		return this.executionOrder;
	}

	/**
	 * @param executionOrder
	 *            the executionOrder to set
	 */
	public void setExecutionOrder(List<Action> executionOrder) {
		this.executionOrder = executionOrder;
	}

	public URI getDeployablesFolderURI() {
		return this.deployablesFolderURI;
	}

	public void setDeployablesFolderURI(URI deployablesFolderURI) {
		this.deployablesFolderURI = deployablesFolderURI;
	}

}
