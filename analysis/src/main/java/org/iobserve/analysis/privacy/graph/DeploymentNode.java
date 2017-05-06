package org.iobserve.analysis.privacy.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is a model of a fully specified pcm resource container for the
 * purpose of privacy analysis.
 * 
 * @author Philipp Weimann
 */
public class DeploymentNode {

	private String resourceContainerID;
	private int isoCountryCode;

	private Set<ComponentNode> containingComponents;

	/**
	 * The constructor
	 * 
	 * @param resourceContainerID
	 *            the id of the represented resource container
	 * @param isoCountryCode
	 *            the (iso) country code of the country the resource container
	 *            is located in
	 */
	public DeploymentNode(String resourceContainerID, int isoCountryCode) {
		this.resourceContainerID = resourceContainerID;
		this.isoCountryCode = isoCountryCode;

		this.containingComponents = new HashSet<ComponentNode>();
	}

	/**
	 * Adds a component to the hosted/deployed components.
	 * 
	 * @param componentNode
	 *            the hosted component
	 * @return whether the adding was successful
	 */
	public boolean addComponent(ComponentNode componentNode) {
		return this.containingComponents.add(componentNode);
	}

	/*
	 * GETTERS
	 */
	/**
	 * @return the Iso Country Code
	 */
	public int getIsoCountryCode() {
		return isoCountryCode;
	}

	/**
	 * @return resource container id
	 */
	public String getResourceContainerID() {
		return resourceContainerID;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Server: " + this.resourceContainerID + " -- Location: " + this.isoCountryCode + "\n");
		sb.append("-Comp:\t ID \t\t\tCompPrivayLvl \tPers \tDeP \t Anonym\n");
		
		for (ComponentNode component : this.containingComponents) {
			sb.append("\t" + component.toString());
		}

		return sb.toString();
	}
	
	
}
