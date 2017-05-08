package org.iobserve.analysis.privacy.graph;

import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

import groovy.ui.Console;

/**
 * 
 * @author Philipp Weimann
 */
public class ComponentEdge {

	private String id;
	private String assemblyConnectorName;
	private ComponentNode providingNode;
	private ComponentNode requiringNode;
	private DataPrivacyLvl privacyLvl;

	public ComponentEdge(String id, String assemblyConnectorName, ComponentNode providingNode, ComponentNode requiringNode, DataPrivacyLvl privacyLvl) {
		this.id = id;
		this.assemblyConnectorName = assemblyConnectorName;
		this.providingNode = providingNode;
		this.requiringNode = requiringNode;
		this.privacyLvl = privacyLvl;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the providingNode
	 */
	public ComponentNode getProvidingNode() {
		return providingNode;
	}

	/**
	 * @return the requiringNode
	 */
	public ComponentNode getRequiringNode() {
		return requiringNode;
	}
	
	public ComponentNode getEdgePartner(ComponentNode firstNode)
	{
		if (firstNode == this.providingNode)
			return this.requiringNode;
		else if (firstNode == this.requiringNode)
			return this.providingNode;
		else
			System.err.println("ERROR: no edge partner found for node: " + firstNode.getAssemblyContextID());
		return null;
	}

	/**
	 * @return the privacyLvl
	 */
	public DataPrivacyLvl getPrivacyLvl() {
		return privacyLvl;
	}
	
	/**
	 * @return the assemblyConnectorName
	 */
	public String getAssemblyConnectorName() {
		return assemblyConnectorName;
	}
}
