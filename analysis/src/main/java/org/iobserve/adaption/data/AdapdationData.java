package org.iobserve.adaption.data;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.graph.ModelGraph;

public class AdapdationData {

	private URI runtimeModelURI;
	private ModelGraph runtimeGraph;

	private URI reDeploymentURI;
	private ModelGraph reDeploymentGraph;

	
	////////////////////// GETTERS & SETTERS //////////////////////
	/**
	 * @return the runtimeModelURI
	 */
	public URI getRuntimeModelURI() {
		return runtimeModelURI;
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
		return runtimeGraph;
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
		return reDeploymentURI;
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
		return reDeploymentGraph;
	}

	/**
	 * @param reDeploymentGraph
	 *            the reDeploymentGraph to set
	 */
	public void setReDeploymentGraph(ModelGraph reDeploymentGraph) {
		this.reDeploymentGraph = reDeploymentGraph;
	}

}
